<div id="top">

<!-- HEADER STYLE: CLASSIC -->
<div align="center">


# VETERINARYCUE-API-BACKEND

<em>Transforming Veterinary Care Through Seamless Innovation</em>

<!-- BADGES -->
<img src="https://img.shields.io/github/license/Nicolascuervor/VeterinaryCUE-API-Backend?style=flat&logo=opensourceinitiative&logoColor=white&color=0080ff" alt="license">
<img src="https://img.shields.io/github/last-commit/Nicolascuervor/VeterinaryCUE-API-Backend?style=flat&logo=git&logoColor=white&color=0080ff" alt="last-commit">
<img src="https://img.shields.io/github/languages/top/Nicolascuervor/VeterinaryCUE-API-Backend?style=flat&color=0080ff" alt="repo-top-language">
<img src="https://img.shields.io/github/languages/count/Nicolascuervor/VeterinaryCUE-API-Backend?style=flat&color=0080ff" alt="repo-language-count">

<em>Built with the tools and technologies:</em>

<img src="https://img.shields.io/badge/Markdown-000000.svg?style=flat&logo=Markdown&logoColor=white" alt="Markdown">
<img src="https://img.shields.io/badge/Spring-000000.svg?style=flat&logo=Spring&logoColor=white" alt="Spring">
<img src="https://img.shields.io/badge/XML-005FAD.svg?style=flat&logo=XML&logoColor=white" alt="XML">

</div>
<br>

---

## Table of Contents

- [Overview](#overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Usage](#usage)
    - [Testing](#testing)
- [Features](#features)
- [Project Structure](#project-structure)
    - [Project Index](#project-index)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgment](#acknowledgment)

---

## Overview

VeterinaryCUE-API-Backend is a comprehensive microservices platform designed to streamline veterinary clinic operations using Spring Boot and Spring Cloud. It provides a modular, scalable architecture that manages everything from appointments and pet records to inventory and invoicing.

**Why VeterinaryCUE-API-Backend?**

This project simplifies building and maintaining a resilient veterinary management system. The core features include:

- 🧩 **🔍 Service Discovery:** Utilizes Eureka for dynamic registration and lookup of microservices, ensuring seamless communication.
- 🔐 **🛡️ Secure Authentication:** Implements OAuth2 and Spring Security for robust user authentication and authorization.
- 🚦 **🌐 API Gateway:** Acts as the central request router, managing traffic efficiently across services.
- ⚙️ **🛠️ Modular Architecture:** Supports multiple interconnected modules like pet data, medical histories, inventory, and billing.
- 🚀 **🌱 Cloud-Ready:** Designed for easy deployment, scaling, and integration within distributed environments.

---

## Features

|      | Component       | Details                                                                                                         |
| :--- | :-------------- | :-------------------------------------------------------------------------------------------------------------- |
| ⚙️  | **Architecture**  | <ul><li>Microservices architecture with distinct services for clinical history, inventory, pets, authentication, etc.</li><li>API Gateway for routing and load balancing.</li><li>Eureka Server for service discovery.</li></ul> |
| 🔩 | **Code Quality**  | <ul><li>Uses Lombok for reducing boilerplate code.</li><li>Consistent Maven project structure across services.</li><li>Adheres to Java best practices, modular service separation.</li></ul> |
| 📄 | **Documentation** | <ul><li>Basic README with project overview.</li><li>Spring Boot and Spring Cloud dependencies documented.</li><li>Potential for Swagger/OpenAPI integration (not explicitly shown).</li></ul> |
| 🔌 | **Integrations**  | <ul><li>Spring Cloud Netflix Eureka for service registry.</li><li>Spring Cloud dependencies for configuration management.</li><li>Potential REST API integrations between services.</li></ul> |
| 🧩 | **Modularity**    | <ul><li>Multiple Maven modules for each service (e.g., historias-clinicas-service, inventario-service).</li><li>Clear separation of concerns per microservice.</li></ul> |
| 🧪 | **Testing**       | <ul><li>Standard Java testing frameworks likely used (JUnit, Mockito).</li><li>Testing structure implied but not explicitly detailed in the codebase.</li></ul> |
| ⚡️  | **Performance**   | <ul><li>Spring Boot optimizations.</li><li>Service discovery and load balancing via Eureka.</li><li>Potential for caching strategies (not explicitly shown).</li></ul> |
| 🛡️ | **Security**      | <ul><li>Authentication service suggests security layer.</li><li>Spring Security likely integrated (not explicitly detailed).</li></ul> |
| 📦 | **Dependencies**  | <ul><li>Primarily Java with Spring Boot and Spring Cloud dependencies.</li><li>Lombok for code reduction.</li><li>Maven as build tool.</li></ul> |

---

## Project Structure

```sh
└── VeterinaryCUE-API-Backend/
    ├── HELP.md
    ├── README.md
    ├── administration-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── api-gateway
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── authentication-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── citas-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── eureka-server
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── facturas-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── historias-clinicas-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── inventario-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── mascotas-service
    │   ├── .gitattributes
    │   ├── .gitignore
    │   ├── .mvn
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   └── src
    ├── mvnw
    ├── mvnw.cmd
    └── pom.xml
```

---

### Project Index

<details open>
	<summary><b><code>VETERINARYCUE-API-BACKEND/</code></b></summary>
	<!-- __root__ Submodule -->
	<details>
		<summary><b>__root__</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ __root__</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/HELP.md'>HELP.md</a></b></td>
					<td style='padding: 8px;'>- Provides essential guidance for setting up and understanding the project’s build and deployment processes, emphasizing Maven and Spring Boot configurations<br>- It ensures developers can correctly initialize, build, and package the application, facilitating smooth integration within the overall architecture<br>- The documentation supports maintaining consistency and clarity across the codebase’s structure and deployment workflows.</td>
				</tr>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/README.md'>README.md</a></b></td>
					<td style='padding: 8px;'>- Provides an overview of the VeterinaryCUE-API-Backend, serving as the core interface for managing the veterinary clinic’s operations<br>- It facilitates seamless communication between clients and the clinic’s data systems, enabling efficient handling of appointments, patient records, and administrative tasks within the overall architecture of the clinic’s digital infrastructure.</td>
				</tr>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the projects core structure and dependency management for a microservices-based veterinary clinic system<br>- Coordinates modules such as API gateway, authentication, and various service components, ensuring consistent versions and integrations within the Spring Boot and Spring Cloud ecosystem<br>- Facilitates seamless orchestration and scalability across the distributed architecture.</td>
				</tr>
			</table>
		</blockquote>
	</details>
	<!-- historias-clinicas-service Submodule -->
	<details>
		<summary><b>historias-clinicas-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ historias-clinicas-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/historias-clinicas-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the project configuration for a microservice managing pet medical histories, integrating core functionalities such as data persistence, security, OAuth2 authentication, and service discovery within a Spring Boot ecosystem<br>- Facilitates seamless interaction with a MySQL database and other microservices, supporting scalable, secure, and maintainable healthcare data management for veterinary applications.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ historias-clinicas-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ historias-clinicas-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ historias-clinicas-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ historias-clinicas-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ historias-clinicas-service.src.main.java.co.cue</b></code>
													<!-- historias_clinicas_service Submodule -->
													<details>
														<summary><b>historias_clinicas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ historias-clinicas-service.src.main.java.co.cue.historias_clinicas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/historias-clinicas-service/src/main/java/co/cue/historias_clinicas_service/HistoriasClinicasServiceApplication.java'>HistoriasClinicasServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and configure the core Spring Boot application for the clinical histories service, enabling seamless startup and integration within the overall architecture<br>- It serves as the entry point that bootstraps the microservice, facilitating communication with other components and ensuring the service is ready to handle clinical history management functions within the healthcare platform.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ historias-clinicas-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ historias-clinicas-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ historias-clinicas-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ historias-clinicas-service.src.test.java.co.cue</b></code>
													<!-- historias_clinicas_service Submodule -->
													<details>
														<summary><b>historias_clinicas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ historias-clinicas-service.src.test.java.co.cue.historias_clinicas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/historias-clinicas-service/src/test/java/co/cue/historias_clinicas_service/HistoriasClinicasServiceApplicationTests.java'>HistoriasClinicasServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validates the applications context loading within the overall healthcare information system, ensuring that the core components of the clinical history management service initialize correctly<br>- Serves as a foundational test to confirm the stability of the service environment, supporting reliable operation and integration within the broader architecture of the healthcare platform.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- inventario-service Submodule -->
	<details>
		<summary><b>inventario-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ inventario-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/inventario-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the inventory management service for the veterinary clinic, enabling efficient tracking, updating, and retrieval of inventory data<br>- Integrates with core system components such as security, service discovery, and database connectivity, supporting seamless operation within the broader microservices architecture<br>- Facilitates inventory-related functionalities essential for maintaining optimal stock levels and resource allocation.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ inventario-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ inventario-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ inventario-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ inventario-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ inventario-service.src.main.java.co.cue</b></code>
													<!-- inventario_service Submodule -->
													<details>
														<summary><b>inventario_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ inventario-service.src.main.java.co.cue.inventario_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/inventario-service/src/main/java/co/cue/inventario_service/InventarioServiceApplication.java'>InventarioServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Bootstraps the inventory service application within the overall system architecture, enabling the initialization and configuration of the Spring Boot framework<br>- It serves as the entry point for launching the inventory management microservice, facilitating seamless integration and operation within a distributed environment focused on tracking and managing inventory data.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ inventario-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ inventario-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ inventario-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ inventario-service.src.test.java.co.cue</b></code>
													<!-- inventario_service Submodule -->
													<details>
														<summary><b>inventario_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ inventario-service.src.test.java.co.cue.inventario_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/inventario-service/src/test/java/co/cue/inventario_service/InventarioServiceApplicationTests.java'>InventarioServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validates the applications context loading within the inventory service, ensuring the Spring Boot environment initializes correctly<br>- Serves as a foundational test to confirm that the core application setup is functional, supporting reliable deployment and integration within the overall system architecture<br>- This test helps maintain stability and readiness for further feature development and integration.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- mascotas-service Submodule -->
	<details>
		<summary><b>mascotas-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ mascotas-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/mascotas-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the dependencies and configuration for the mascotas-service, a core component responsible for managing pet-related data within the veterinary clinic system<br>- It integrates Spring Boot functionalities such as data persistence, security, and service discovery, enabling seamless interaction with other microservices and secure handling of pet information in the overall architecture.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ mascotas-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ mascotas-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ mascotas-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ mascotas-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ mascotas-service.src.main.java.co.cue</b></code>
													<!-- mascotas_service Submodule -->
													<details>
														<summary><b>mascotas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ mascotas-service.src.main.java.co.cue.mascotas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/mascotas-service/src/main/java/co/cue/mascotas_service/MascotasServiceApplication.java'>MascotasServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and bootstrap the mascotas service application within a Spring Boot environment, enabling the deployment and execution of the microservice responsible for managing pet-related data<br>- Serves as the entry point that launches the entire application, integrating core configurations and setting up the runtime context for the mascotas service architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ mascotas-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ mascotas-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ mascotas-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ mascotas-service.src.test.java.co.cue</b></code>
													<!-- mascotas_service Submodule -->
													<details>
														<summary><b>mascotas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ mascotas-service.src.test.java.co.cue.mascotas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/mascotas-service/src/test/java/co/cue/mascotas_service/MascotasServiceApplicationTests.java'>MascotasServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Verifies the proper loading and initialization of the MascotasService application context within the overall system architecture<br>- Ensures that core components and configurations are correctly set up, facilitating reliable startup behavior and integration readiness for the mascotas-service module<br>- This foundational test supports the stability and integrity of the applications deployment and runtime environment.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- eureka-server Submodule -->
	<details>
		<summary><b>eureka-server</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ eureka-server</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/eureka-server/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the configuration for the Eureka discovery server within the microservices architecture, enabling service registration and discovery to facilitate dynamic inter-service communication and scalability across the system<br>- It integrates Spring Cloud Netflix Eureka to support resilient, loosely coupled service interactions in the overall application ecosystem.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ eureka-server.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ eureka-server.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ eureka-server.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ eureka-server.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ eureka-server.src.main.java.co.cue</b></code>
													<!-- eureka_server Submodule -->
													<details>
														<summary><b>eureka_server</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ eureka-server.src.main.java.co.cue.eureka_server</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/eureka-server/src/main/java/co/cue/eureka_server/EurekaServerApplication.java'>EurekaServerApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and launch the Eureka server, serving as the central registry for microservices within the architecture<br>- It facilitates service discovery, enabling dynamic registration and lookup of services, which supports scalable and resilient communication among distributed components in the system.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ eureka-server.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ eureka-server.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ eureka-server.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ eureka-server.src.test.java.co.cue</b></code>
													<!-- eureka_server Submodule -->
													<details>
														<summary><b>eureka_server</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ eureka-server.src.test.java.co.cue.eureka_server</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/eureka-server/src/test/java/co/cue/eureka_server/EurekaServerApplicationTests.java'>EurekaServerApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validates the proper loading and initialization of the Eureka server applications context within the overall microservices architecture<br>- Ensures that the core service registry functions correctly, supporting service discovery and registration processes essential for seamless communication among distributed components in the system.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- authentication-service Submodule -->
	<details>
		<summary><b>authentication-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ authentication-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/authentication-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the dependencies and configuration for the authentication and user management microservice within the overall architecture<br>- Facilitates secure user authentication, authorization, and integration with other services through OAuth2, Spring Security, and Eureka client, supporting seamless and scalable identity management across the distributed system.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ authentication-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ authentication-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ authentication-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ authentication-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ authentication-service.src.main.java.co.cue</b></code>
													<!-- auth Submodule -->
													<details>
														<summary><b>auth</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ authentication-service.src.main.java.co.cue.auth</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/authentication-service/src/main/java/co/cue/auth/AuthenticationServiceApplication.java'>AuthenticationServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Bootstraps the authentication service within the overall architecture, enabling user authentication and security management<br>- It initializes the Spring Boot application, serving as the entry point for deploying and running the authentication component, which integrates with other microservices to facilitate secure access control across the system.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ authentication-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ authentication-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ authentication-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ authentication-service.src.test.java.co.cue</b></code>
													<!-- auth Submodule -->
													<details>
														<summary><b>auth</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ authentication-service.src.test.java.co.cue.auth</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/authentication-service/src/test/java/co/cue/auth/AuthenticationServiceApplicationTests.java'>AuthenticationServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validate the applications context loading within the authentication service, ensuring the Spring Boot environment initializes correctly<br>- Serves as a foundational health check to confirm that core components are properly configured, supporting overall system stability and readiness for further development and integration within the broader microservices architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- api-gateway Submodule -->
	<details>
		<summary><b>api-gateway</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ api-gateway</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/api-gateway/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the main routing and gateway service within the architecture, facilitating centralized request management and service discovery<br>- Integrates with Eureka for dynamic service registration, ensuring seamless communication between microservices in the overall system<br>- Acts as the primary entry point, directing client requests efficiently across the distributed application environment.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ api-gateway.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ api-gateway.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ api-gateway.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ api-gateway.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ api-gateway.src.main.java.co.cue</b></code>
													<!-- api_gateway Submodule -->
													<details>
														<summary><b>api_gateway</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ api-gateway.src.main.java.co.cue.api_gateway</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/api-gateway/src/main/java/co/cue/api_gateway/ApiGatewayApplication.java'>ApiGatewayApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and bootstrap the API Gateway service within the overall architecture, enabling centralized routing, request handling, and integration with downstream microservices<br>- It serves as the primary entry point for client interactions, ensuring secure, scalable, and efficient communication across the distributed system<br>- This setup is essential for managing API traffic and orchestrating service interactions in the architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ api-gateway.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ api-gateway.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ api-gateway.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ api-gateway.src.test.java.co.cue</b></code>
													<!-- api_gateway Submodule -->
													<details>
														<summary><b>api_gateway</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ api-gateway.src.test.java.co.cue.api_gateway</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/api-gateway/src/test/java/co/cue/api_gateway/ApiGatewayApplicationTests.java'>ApiGatewayApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validate the application context loads successfully within the API Gateway project, ensuring the overall systems foundational components initialize correctly<br>- This test confirms that the Spring Boot environment and essential configurations are properly set up, supporting reliable operation and integration of the gateway with other microservices in the architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- citas-service Submodule -->
	<details>
		<summary><b>citas-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ citas-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/citas-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the dependencies and configuration for the citas-service, a core component responsible for managing veterinary appointment scheduling within the larger microservices architecture<br>- It integrates data persistence, security, service discovery, and OAuth2 authentication, enabling seamless, secure handling of patient appointment data in a scalable, cloud-ready environment.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ citas-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ citas-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ citas-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ citas-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ citas-service.src.main.java.co.cue</b></code>
													<!-- citas_service Submodule -->
													<details>
														<summary><b>citas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ citas-service.src.main.java.co.cue.citas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/citas-service/src/main/java/co/cue/citas_service/CitasServiceApplication.java'>CitasServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and bootstrap the citas-service application within the overall system architecture, enabling the setup of the Spring Boot environment<br>- It serves as the entry point for deploying the service, orchestrating the startup process and integrating core components essential for managing appointment scheduling functionalities in the larger platform.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ citas-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ citas-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ citas-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ citas-service.src.test.java.co.cue</b></code>
													<!-- citas_service Submodule -->
													<details>
														<summary><b>citas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ citas-service.src.test.java.co.cue.citas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/citas-service/src/test/java/co/cue/citas_service/CitasServiceApplicationTests.java'>CitasServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validates the applications context loading within the overall service architecture, ensuring that the core Spring Boot environment initializes correctly<br>- Serves as a foundational test to confirm that the Citas Service can start up successfully, supporting reliable deployment and integration within the broader system<br>- This test helps maintain stability and readiness for further feature development and integration.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- administration-service Submodule -->
	<details>
		<summary><b>administration-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ administration-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/administration-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the core dependencies and configuration for the administration service, enabling data management, security, and service discovery within the veterinary clinics architecture<br>- It facilitates secure data handling, integration with other microservices via Eureka, and supports RESTful operations, forming a foundational component for managing statistical data and administrative functionalities in the overall system.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ administration-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ administration-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ administration-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ administration-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ administration-service.src.main.java.co.cue</b></code>
													<!-- administration_service Submodule -->
													<details>
														<summary><b>administration_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ administration-service.src.main.java.co.cue.administration_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/administration-service/src/main/java/co/cue/administration_service/AdministrationServiceApplication.java'>AdministrationServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Bootstraps the administration service within the overall architecture, enabling the application to initialize and run as a Spring Boot microservice<br>- It serves as the entry point for deploying administrative functionalities, ensuring seamless integration and startup within the broader system ecosystem<br>- This setup facilitates centralized management and operational control across the platform.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ administration-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ administration-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ administration-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ administration-service.src.test.java.co.cue</b></code>
													<!-- administration_service Submodule -->
													<details>
														<summary><b>administration_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ administration-service.src.test.java.co.cue.administration_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/administration-service/src/test/java/co/cue/administration_service/AdministrationServiceApplicationTests.java'>AdministrationServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validate the applications context loading within the administration service, ensuring the Spring Boot environment initializes correctly<br>- Serves as a foundational test to confirm that the core application components are properly configured and can start without errors, supporting overall system stability and readiness for further development and integration within the broader architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<!-- facturas-service Submodule -->
	<details>
		<summary><b>facturas-service</b></summary>
		<blockquote>
			<div class='directory-path' style='padding: 8px 0; color: #666;'>
				<code><b>⦿ facturas-service</b></code>
			<table style='width: 100%; border-collapse: collapse;'>
			<thead>
				<tr style='background-color: #f8f9fa;'>
					<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
					<th style='text-align: left; padding: 8px;'>Summary</th>
				</tr>
			</thead>
				<tr style='border-bottom: 1px solid #eee;'>
					<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/facturas-service/pom.xml'>pom.xml</a></b></td>
					<td style='padding: 8px;'>- Defines the dependencies and configuration for the facturas-service, a core component responsible for generating and managing invoices within the system architecture<br>- It integrates essential Spring Boot modules, security, database connectivity, and service discovery, enabling seamless invoice processing and ensuring secure, scalable interactions with other microservices in the platform.</td>
				</tr>
			</table>
			<!-- src Submodule -->
			<details>
				<summary><b>src</b></summary>
				<blockquote>
					<div class='directory-path' style='padding: 8px 0; color: #666;'>
						<code><b>⦿ facturas-service.src</b></code>
					<!-- main Submodule -->
					<details>
						<summary><b>main</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ facturas-service.src.main</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ facturas-service.src.main.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ facturas-service.src.main.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ facturas-service.src.main.java.co.cue</b></code>
													<!-- facturas_service Submodule -->
													<details>
														<summary><b>facturas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ facturas-service.src.main.java.co.cue.facturas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/facturas-service/src/main/java/co/cue/facturas_service/FacturasServiceApplication.java'>FacturasServiceApplication.java</a></b></td>
																	<td style='padding: 8px;'>- Initialize and bootstrap the Facturas Service application within a Spring Boot environment, enabling the setup of the invoice management microservice<br>- It serves as the entry point for launching the service, orchestrating the startup process and integrating core configurations essential for handling invoice-related operations within the overall system architecture.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<!-- test Submodule -->
					<details>
						<summary><b>test</b></summary>
						<blockquote>
							<div class='directory-path' style='padding: 8px 0; color: #666;'>
								<code><b>⦿ facturas-service.src.test</b></code>
							<!-- java Submodule -->
							<details>
								<summary><b>java</b></summary>
								<blockquote>
									<div class='directory-path' style='padding: 8px 0; color: #666;'>
										<code><b>⦿ facturas-service.src.test.java</b></code>
									<!-- co Submodule -->
									<details>
										<summary><b>co</b></summary>
										<blockquote>
											<div class='directory-path' style='padding: 8px 0; color: #666;'>
												<code><b>⦿ facturas-service.src.test.java.co</b></code>
											<!-- cue Submodule -->
											<details>
												<summary><b>cue</b></summary>
												<blockquote>
													<div class='directory-path' style='padding: 8px 0; color: #666;'>
														<code><b>⦿ facturas-service.src.test.java.co.cue</b></code>
													<!-- facturas_service Submodule -->
													<details>
														<summary><b>facturas_service</b></summary>
														<blockquote>
															<div class='directory-path' style='padding: 8px 0; color: #666;'>
																<code><b>⦿ facturas-service.src.test.java.co.cue.facturas_service</b></code>
															<table style='width: 100%; border-collapse: collapse;'>
															<thead>
																<tr style='background-color: #f8f9fa;'>
																	<th style='width: 30%; text-align: left; padding: 8px;'>File Name</th>
																	<th style='text-align: left; padding: 8px;'>Summary</th>
																</tr>
															</thead>
																<tr style='border-bottom: 1px solid #eee;'>
																	<td style='padding: 8px;'><b><a href='https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/master/facturas-service/src/test/java/co/cue/facturas_service/FacturasServiceApplicationTests.java'>FacturasServiceApplicationTests.java</a></b></td>
																	<td style='padding: 8px;'>- Validates the applications context loading within the overall facturas-service architecture, ensuring that core components initialize correctly<br>- Serves as a foundational test to confirm the integrity of the Spring Boot environment, supporting reliable deployment and operation of the service responsible for managing invoice-related functionalities.</td>
																</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
</details>

---

## Getting Started

### Prerequisites

This project requires the following dependencies:

- **Programming Language:** Java
- **Package Manager:** Maven

### Installation

Build VeterinaryCUE-API-Backend from the source and install dependencies:

1. **Clone the repository:**

    ```sh
    ❯ git clone https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend
    ```

2. **Navigate to the project directory:**

    ```sh
    ❯ cd VeterinaryCUE-API-Backend
    ```

3. **Install the dependencies:**

**Using [maven](https://maven.apache.org/):**

```sh
❯ mvn install
```

### Usage

Run the project with:

**Using [maven](https://maven.apache.org/):**

```sh
mvn exec:java
```

### Testing

Veterinarycue-api-backend uses the {__test_framework__} test framework. Run the test suite with:

**Using [maven](https://maven.apache.org/):**

```sh
mvn test
```

---

## Roadmap

- [X] **`Task 1`**: <strike>Implement feature one.</strike>
- [ ] **`Task 2`**: Implement feature two.
- [ ] **`Task 3`**: Implement feature three.

---

## Contributing

- **💬 [Join the Discussions](https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/issues)**: Submit bugs found or log feature requests for the `VeterinaryCUE-API-Backend` project.
- **💡 [Submit Pull Requests](https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/Nicolascuervor/VeterinaryCUE-API-Backend
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="left">
   <a href="https://github.com{/Nicolascuervor/VeterinaryCUE-API-Backend/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=Nicolascuervor/VeterinaryCUE-API-Backend">
   </a>
</p>
</details>

---

## License

Veterinarycue-api-backend is protected under the [LICENSE](https://choosealicense.com/licenses) License. For more details, refer to the [LICENSE](https://choosealicense.com/licenses/) file.

---

## Acknowledgments

- Credit `contributors`, `inspiration`, `references`, etc.

<div align="left"><a href="#top">⬆ Return</a></div>

---
