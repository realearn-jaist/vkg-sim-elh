# similarity-mapping-obda# Mapping Service

![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0%2B-green.svg)
![Gradle](https://img.shields.io/badge/Gradle-7.0%2B-blue.svg)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## Overview

The Mapping Service is a Java-based web application that allows users to add new mappings to an existing mapping file based on a similarity file and a user-defined threshold value. This service is built using Java, Thymeleaf, and Gradle.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Adding Mappings](#adding-mappings)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- Load mapping files in a user-friendly interface.
- Upload similarity files and set a threshold for adding new mappings.
- View the original mapping and the result with added mappings.
- Download the updated mapping.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 8 or higher installed.
- Gradle 7.0 or higher installed.
- A modern web browser (Chrome, Firefox, Edge, etc.).

### Installation

1. Clone this repository:
`git clone https://github.com/your-username/mapping-service.git`

2. Change to the project directory:
`cd mapping-service` 

3. Build and run the project (MappingApplication.java)


4. Open your web browser and navigate to `http://localhost:8080` to access the Mapping Service.

## Usage

### Adding Mappings

1. Click the "Load Mapping File (.obda)" button to upload your existing mapping file.

2. Click the "Load Similarity File" button to upload your similarity file. The similarity file used in this project is based on the [Sim-Preference-ELH](https://github.com/realearn-jaist/sim-preference-elh.git) project.

3. Set the threshold value. Only mappings with a similarity degree greater than this value will be added.

4. Click the "Perform Mapping" button to add new mappings based on the similarity file and threshold.

5. View the original mapping and the result with added mappings.

6. Click the "Download Mapping" link to download the updated mapping.

## Contributing

Contributions are welcome!

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

We would like to thank the open-source community for their contributions and support.

