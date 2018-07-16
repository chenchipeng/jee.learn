1.修改src/test/resources目录下hibernate.cfg.xml文件中数据库连接信息

2.修改pom.xml中hibernate3-maven-plugin的packagename=指定的model包名

3.执行mvn hibernate3:hbm2java命令，自动生成数据库映射对象

			<!-- hbm3 to java -->
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>hibernate3-maven-plugin</artifactId>
				<version>3.0</version>
				<configuration>
					<hibernatetool destdir="src/test/java">
						<jdbcconfiguration
							configurationfile="src/test/resources/hbm2java/hibernate.cfg.xml"
							revengfile="src/test/resources/hbm2java/hibernate.reveng.xml"
							packagename="com.jee.learn.generate" />
						<hbm2java jdk5="true" ejb3="true" />
					</hibernatetool>
				</configuration>
				<dependencies>
					<dependency>
						<groupId>org.slf4j</groupId>
						<artifactId>slf4j-log4j12</artifactId>
						<version>1.7.12</version>
					</dependency>
					<dependency>
						<groupId>mysql</groupId>
						<artifactId>mysql-connector-java</artifactId>
						<version>5.1.21</version>
					</dependency>
					<dependency>
						<groupId>org.hibernate</groupId>
						<artifactId>hibernate-core</artifactId>
						<version>3.3.2.GA</version>
					</dependency>
					<dependency>
						<groupId>cglib</groupId>
						<artifactId>cglib</artifactId>
						<version>2.2.2</version>
					</dependency>
					<dependency>
						<groupId>asm</groupId>
						<artifactId>asm-util</artifactId>
						<version>3.3.1</version>
						<exclusions>
							<exclusion>
								<groupId>asm</groupId>
								<artifactId>asm-tree</artifactId>
							</exclusion>
						</exclusions>
					</dependency>
				</dependencies>
			</plugin>