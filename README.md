# TransferMoneyApi

This project provides demonstration of RESTful API designed for money transfers between accounts.

#### Components

_Backend (server-side)_ - provides money transfer and backing functionality via RESTful API.

_Loader (client-side)_ - emulates loading and performance via RESTful API.

#### Usage

> How to build?

_Launch the command under the project root directory:_ 

`${ROOT_DIR}/gradle packageDelivery`

> Where are builds?

_Builds reside under the directories:_

`${ROOT_DIR}/server/build/dist/transfer-money-backend-*.zip`

`${ROOT_DIR}/client/build/dist/transfer-money-loader-*.zip`

> How to run demo after builds have been unzipped?

_Navigate to unzipped directory and launch_ `run.bat` _or_ `run.sh`.

> Does it make sense what runs first - backend or loader?

_Yes. Loader must be run only after backend's been run._

> How to customize backend or loader?

_Configuration file_ `server.properties` _defines reasonable settings._

#### Requirements

Java 8+

Gradle 4.6+