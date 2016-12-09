/*
 * *
 *  * Copyright 2017 Red Hat, Inc, and individual contributors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package org.wildfly.swarm.orientdb.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.ResourceLoaders;
import org.wildfly.swarm.bootstrap.modules.DynamicModuleFinder;
import org.wildfly.swarm.orientdb.OrientDBFraction;

/**
 * Auto-detection for OrientDB NoSQL driver (based on org.wildfly.swarm.datasources.runtime.DriverInfo, thanks Bob!).
 *
 * <p>mark as a {@link javax.inject.Singleton}, due
 * to the fact that state is retained as to if the driver has or has not
 * been detected.</p>
 * TODO: come up with a test case that would cause the driver to attempt to be detected more than once.
 *
 * @author Scott Marlow
 */
@ApplicationScoped
public class OrientDBDriverInfo {

    private static final String FILE_PREFIX = "file:";
    private static final String JAR_FILE_PREFIX = "jar:file:";
    private final String name;
    private final ModuleIdentifier moduleIdentifier;
    private final String detectableClassName;
    private final String[] optionalClassNames;
    private boolean installed;

    public OrientDBDriverInfo() {
        this.name = "OrientDB";
        this.moduleIdentifier = ModuleIdentifier.create("com.orientechnologies");
        this.detectableClassName = "com.orientechnologies.orient.core.db.OPartitionedDatabasePool";
        this.optionalClassNames = new String[] {
                "com.google.common.util.concurrent.AsyncFunction",
                "com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap",
                "com.tinkerpop.blueprints.Graph",
                "com.tinkerpop.blueprints.impls.orient.OrientVertex",
                "com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx",
                "com.orientechnologies.orient.client.remote.ORemoteConnectionPool",
                "com.orientechnologies.orient.object.db.OObjectDatabaseTx",
                "com.orientechnologies.orient.server.distributed.impl.task.OSyncClusterTask"
        };
    }

    public String name() {
        return this.name;
    }

    public boolean detect(OrientDBFraction fraction) {
        if (fraction.subresources().orient(this.name) != null) { // TODO: verify this already installed check
            // already installed
            return true;
        }

        Messages.MESSAGES.attemptToAutoDetectDriver(this.name);

        File primaryJar = attemptDetection();

        if (primaryJar != null) {
            Set<File> optionalJars = findOptionalJars();

            optionalJars.add(primaryJar);
            DynamicModuleFinder.register(this.moduleIdentifier, (id, loader) -> {
                ModuleSpec.Builder builder = ModuleSpec.build(id);

                for (File eachJar : optionalJars) {

                    try {
                        JarFile jar = new JarFile(eachJar);
                        builder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
                                ResourceLoaders.createIterableJarResourceLoader(jar.getName(), jar)
                        ));
                    } catch (IOException e) {
                        Messages.MESSAGES.errorLoadingAutodetectedDriver(this.name, e);
                        return null;
                    }
                }

                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("javax.api")));
                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("org.picketbox")));
                // Cassandra driver needs sun.misc.Unsafe from sun.jdk
                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("sun.jdk")));
                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("org.slf4j")));
                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("javax.transaction.api")));
                builder.addDependency(DependencySpec.createLocalDependencySpec());

                return builder.create();
            });

            this.installed = true;
        }

        return this.installed;
    }

    private File attemptDetection() {
        return findLocationOfClass(this.detectableClassName);
    }

    private Set<File> findOptionalJars() {
        Set<File> optionalJars = new HashSet<>();

        if (this.optionalClassNames != null) {
            for (String each : this.optionalClassNames) {
                File file = findLocationOfClass(each);
                if (file != null) {
                    optionalJars.add(file);
                }
            }
        }

        return optionalJars;
    }

    private File findLocationOfClass(String className) {
        try {
            ClassLoader cl = Module.getBootModuleLoader().loadModule(ModuleIdentifier.create("swarm.application")).getClassLoader();
            File candidate = findLocationOfClass(cl, className);
            if (candidate == null) {
                candidate = findLocationOfClass(ClassLoader.getSystemClassLoader(), className);
            }

            return candidate;
        } catch (ModuleLoadException e) {
            // ignore
        } catch (IOException e) {
            Messages.MESSAGES.errorLoadingAutodetectedDriver(this.name, e);
        }

        return null;
    }

    private File findLocationOfClass(ClassLoader classLoader, String className) throws IOException {

        try {
            Class<?> driverClass = classLoader.loadClass(className);

            URL location = driverClass.getProtectionDomain().getCodeSource().getLocation();

            String locationStr = location.toExternalForm();
            if (locationStr.startsWith(JAR_FILE_PREFIX)) {
                locationStr = locationStr.substring(JAR_FILE_PREFIX.length());
            } else if (locationStr.startsWith(FILE_PREFIX)) {
                locationStr = locationStr.substring(FILE_PREFIX.length());
            }

            int bangLoc = locationStr.indexOf('!');
            if (bangLoc >= 0) {
                locationStr = locationStr.substring(0, bangLoc);
            }

            locationStr = getPlatformPath(locationStr);

            File locationFile = Paths.get(locationStr).toFile();

            return locationFile;
        } catch (ClassNotFoundException e) {
            // ignore;
        }

        return null;
    }

    protected String getPlatformPath(String path) {
        if (!isWindows()) {
            return path;
        }

        URI uri = URI.create("file://" + path);
        return Paths.get(uri).toString();
    }

    protected boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public boolean isInstalled() {
        return this.installed;
    }

    public String toString() {
        return "[NoSQLDriverInfo: detectable=" + this.detectableClassName + "]";
    }
}

