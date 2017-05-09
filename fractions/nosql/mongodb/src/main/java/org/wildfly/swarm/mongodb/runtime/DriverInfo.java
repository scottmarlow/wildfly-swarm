/*
 *
 *  * JBoss, Home of Professional Open Source.
 *  * Copyright 2017, Red Hat, Inc., and individual contributors
 *  * as indicated by the @author tags. See the copyright.txt file in the
 *  * distribution for a full listing of individual contributors.
 *  *
 *  * This is free software; you can redistribute it and/or modify it
 *  * under the terms of the GNU Lesser General Public License as
 *  * published by the Free Software Foundation; either version 2.1 of
 *  * the License, or (at your option) any later version.
 *  *
 *  * This software is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public
 *  * License along with this software; if not, write to the Free
 *  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 *
 */
package org.wildfly.swarm.mongodb.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.ResourceLoaders;
import org.wildfly.swarm.bootstrap.modules.DynamicModuleFinder;
import org.wildfly.swarm.mongodb.MongoDBFraction;

/**
 * Auto-detection and default DS information for detectable JDBC drivers.
 *
 * <p>For each detectable JDBC driver, a subclass should be created.</p>
 *
 * <p>Each subclass should be marked as a {@link javax.inject.Singleton}, due
 * to the fact that state is retained as to if the driver has or has not
 * been detected.</p>
 *
 * @author Bob McWhirter
 */
public abstract class DriverInfo {

    private final String name;

    private final ModuleIdentifier moduleIdentifier;

    private final String detectableClassName;

    private final String[] optionalClassNames;

    private boolean installed;

    protected DriverInfo(String name,
                         ModuleIdentifier moduleIdentifier,
                         String detectableClassName,
                         String... optionalClassNames) {
        this.name = name;
        this.moduleIdentifier = moduleIdentifier;
        this.detectableClassName = detectableClassName;
        this.optionalClassNames = optionalClassNames;
    }

    private static final String FILE_PREFIX = "file:";

    private static final String JAR_FILE_PREFIX = "jar:file:";

    public String name() {
        return this.name;
    }

    public boolean detect(MongoDBFraction fraction) {
        if (fraction.subresources().mongo(this.name) != null) {
            // already installed
            return true;
        }

        // DatasourcesMessages.MESSAGES.attemptToAutoDetectJdbcDriver(this.name);

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
                        // DatasourcesMessages.MESSAGES.errorLoadingAutodetectedJdbcDriver(this.name, e);
                        e.printStackTrace(); // TODO: log exception
                        return null;
                    }
                }

                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("javax.api")));
                builder.addDependency(DependencySpec.createModuleDependencySpec(ModuleIdentifier.create("org.picketbox")));
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
            // DatasourcesMessages.MESSAGES.errorLoadingAutodetectedJdbcDriver(this.name, e);
            e.printStackTrace();  // todo: log ignored exception
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
        return "[DriverInfo: detectable=" + this.detectableClassName + "]";
    }

    @SuppressWarnings("unchecked")
    public void installDatasource(MongoDBFraction fraction /*, String dsName, DataSourceConsumer config*/) {
        //fraction.dataSource(dsName, (ds) -> {
        //    ds.driverName(this.name);
        //    this.configureDefaultDS(ds);
        //    config.accept(ds);
        //});
    }

}
