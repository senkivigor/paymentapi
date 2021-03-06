# PaymentIQ rest API


Provide rest payment api according to PaymentIQ documentations.

# Installation

The easiest way to install the paymentapi package is either via github:
```sh
git clone https://github.com/senkivigor/paymentapi.git
cd paymentapi/api
mvn install
```
# Examples  
Service is available on:  
**interview-assign-ihor-senkiv.herokuapp.com/:8080**  
**!!!Note:**  
Supported currencies: **EUR, USD, UAH**.  
Users with userId are available: **1, 2, 3**.   

## Example requests
### Verify User
URL: interview-assign-ihor-senkiv.herokuapp.com/:8080/api/verifyuser
#### Success
Example of JSON POST request body: 
```sh
{
    "sessionId": "s9wefk2392masgp",
    "userId": "2"
}
```
Example of JSON  response body: 
```sh
{
    "userId": "2",
    "success": true,
    "userCat": "Regular",
    "kycStatus": "Approved",
    "sex": "FEMALE",
    "firstName": "Cate",
    "lastName": "Brown",
    "street": "Amazing str.",
    "city": "Berlin",
    "zip": "12312",
    "country": "Germany",
    "email": "brown@gmail.com",
    "dob": "1978-03-05",
    "mobile": "67437826437",
    "balance": 2000,
    "balanceCy": "EUR",
    "locale": "de_DE"
}
```
#### Failed
Example of JSON POST request body: 
```sh
{
    "sessionId": "s9wefk2392masgp",
    "userId": "5"
}
```
Example of JSON  response body: 
```sh
{
    "userId": "5",
    "success": false,
    "errCode": 1,
    "errMsg": "Unknown userId"
}
```
### Authorize transaction
URL: interview-assign-ihor-senkiv.herokuapp.com/:8080/api/authorize
#### Success
Example of JSON POST request body: 
```sh
{   
    "userId":"2",
    "txAmount":100,
    "txAmountCy":"EUR",
    "txId":"12232123",
    "txTypeId":101,
    "txName":"deposite",
    "provider":"MasterCard",
    "pspService":"service",
    "originTxId":"213123sdfsdf1",
    "accountId":"9903ED01-A73C-4874-8ABF-D2678E3AE23D",
    "accountHolder":"Vasya",
    "maskedAccount":"411812******2410",
    "pspFee":"12",
    "pspFeeCy":"EUR",
    "pspFeeBase":"13",
    "pspFeeBaseCy":"EUR",
    "attributes":{"a":"a"}
}
```

Example of JSON  response body: 
```sh
{
    "userId": "2",
    "success": true,
    "txId": "12232123",
    "merchantTxId": "1",
    "authCode": "8ba165fb-671e-4e5d-9e90-fad24bfa7ae1"
}
```
#### Failed
Example of JSON POST request body: 
```sh
{   
    "userId":"1",
    "txAmount":-10000,
    "txAmountCy":"EUR",
    "txId":"12232123",
    "txTypeId":101,
    "txName":"deposite",
    "provider":"MasterCard",
    "pspService":"service",
    "originTxId":"213123sdfsdf1",
    "accountId":"9903ED01-A73C-4874-8ABF-D2678E3AE23D",
    "accountHolder":"Vasya",
    "maskedAccount":"411812******2410",
    "pspFee":"12",
    "pspFeeCy":"EUR",
    "pspFeeBase":"13",
    "pspFeeBaseCy":"EUR",
    "attributes":{"a":"a"}
}
```

Example of JSON  response body: 

```sh
{
    "userId": "1",
    "success": false,
    "txId": "12232123",
    "merchantTxId": "2",
    "authCode": "364eceb1-a2ee-4ff2-a4ad-5308b2fe0221",
    "errCode": 3,
    "errMsg": "Transaction is not valid or has been already processed"
}
```

### Transfer transaction
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/transfer
#### Success
Example of JSON POST request body: 
```sh
{
    "userId":"1",
    "authCode":"e635f064-2e06-4fd2-a74a-a991d566ab6c",
    "txAmount":"-100",
    "txAmountCy":"EUR",
    "txPspAmount":"100",
    "txPspAmountCy":"EUR",
    "fee":"0.5",
    "feeCy":"EUR",
    "feeMode":"D",
    "txID":"1234",
    "txTypeId":1,
    "txName":"Tx name",
    "provider":"provider",
    "pspService":"pspservice",
    "txRefId":"1",
    "originTxId":"1",
    "accountId":"c6a70bf3-4227-483b-9b19-7fefee9fd06c",
    "accountHolder":"Holder",
    "maskedAccount":"**********",
    "pspFee":"1",
    "pspFeeCy":"USD",
    "pspFeeBase":"1",
    "pspFeeBaseCy":"EUR",
    "pspRefId":"!",
    "pspStatusMessage":null,
    "attributes":{"userId":null,"userCat":null}
}
```
Example of JSON  response body: 
```sh
{
    "userId": "1",
    "success": true,
    "txId": "1234",
    "merchantTxId": "9",
    "authCode":"e635f064-2e06-4fd2-a74a-a991d566ab6c"
}
```
#### Failed
Example of JSON POST request body: 
```sh
{
    "userId":"2",
    "authCode":null,
    "txAmount":"-100",
    "txAmountCy":"GBP",
    "txPspAmount":"100",
    "txPspAmountCy":"GBP",
    "fee":"0.5",
    "feeCy":"GBP",
    "feeMode":"D",
    "txID":"12",
    "txTypeId":1,
    "txName":"Tx name",
    "provider":"provider",
    "pspService":"pspservice",
    "txRefId":"1",
    "originTxId":"1",
    "accountId":"c6a70bf3-4227-483b-9b19-7fefee9fd06c",
    "accountHolder":"Holder",
    "maskedAccount":"**********",
    "pspFee":"1",
    "pspFeeCy":"USD",
    "pspFeeBase":"1",
    "pspFeeBaseCy":"EUR",
    "pspRefId":"!",
    "pspStatusMessage":null,
    "attributes":{"userId":null,"userCat":null}
}
```
Example of JSON  response body: 
```sh
{
    "userId": "2",
    "success": false,
    "errCode": 4,
    "errMsg": "Currency is not supported"
}
```

### Cancel transaction
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/cancel
#### Success
Example of JSON POST request body: 
```sh
{
    "userId":"1",
    "authCode":"a4842dea-d439-4b09-863d-90c2f04a021f",
    "txAmount":"50",
    "txAmountCy":"EUR",
    "txId":"123",
    "txTypeId":1,
    "txName":"Tx name",
    "provider":"provider",
    "originTxId":"1",
    "accountId":"5a066663-0c77-4039-b3a3-72db9e4b1057",
    "maskedAccount":"**********",
    "statusCode":"statuscode",
    "pspStatusCode":"spsstatuscode",
    "pspFee":"10",
    "pspFeeCy":"EUR",
    "pspFeeBase":"feebase",
    "pspFeeBaseCy":"EUR",
    "pspRefId":"1",
    "pspStatusMessage":"message",
    "attributes":{}
}
```
Example of JSON  response body: 
```sh
{
    "userId": "1",
    "success": true,
    "txId":"123",
    "merchantTxId": "1",
    "authCode": "46607ee1-ec18-4d3e-a959-e6b2a94e6f8f"
}
```
#### Failed
Example of JSON POST request body: 
```sh
{
    "userId":"2",
    "authCode":"2146f93f-5d69-4a7b-b933-81dfd8f78f55",
    "txAmount":"50",
    "txAmountCy":"EUR",
    "txId":"123",
    "txTypeId":1,
    "txName":"Tx name",
    "provider":"provider",
    "originTxId":"1",
    "accountId":"5a066663-0c77-4039-b3a3-72db9e4b1057",
    "maskedAccount":"**********",
    "statusCode":"statuscode",
    "pspStatusCode":"spsstatuscode",
    "pspFee":"10",
    "pspFeeCy":"EUR",
    "pspFeeBase":"feebase",
    "pspFeeBaseCy":"EUR",
    "pspRefId":"1",
    "pspStatusMessage":"message",
    "attributes":{}
}
```
Example of JSON  response body: 
```sh
{
    "userId": "2",
    "txId":"123",
    "authCode":"2146f93f-5d69-4a7b-b933-81dfd8f78f55",
    "success": false,
    "errCode": 3,
    "errMsg": "Transaction is not valid or has been already processed"
}
```

### Find all transactions filtered by userId and txAmountCy
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/transactions

Method: GET

Parameters:  
userId (optional)  
txAmountCy(optional)

Example of request URL: 
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/transactions/?userId=2&txAmountCy=EUR

Example of JSON  response body: 

```sh
[
    {
        "id": 4,
        "txId": "345",
        "user": "2",
        "txAmount": 100,
        "txAmountCy": "EUR",
        "fee": null,
        "feeCy": null,
        "feeMode": null,
        "authCode": "fac5a4b3-185a-4c1b-9daa-e16a49476588",
        "transactionStatus": "PENDING"
    },
    {
        "id": 1,
        "txId": "123",
        "user": "2",
        "txAmount": 100,
        "txAmountCy": "EUR",
        "fee": null,
        "feeCy": null,
        "feeMode": null,
        "authCode": "261984c7-10f4-4cb8-856c-768f72b4c675",
        "transactionStatus": "PENDING"
    }
]

```

### Find transaction by txId
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/transactions/{txId}

Method: GET

Example of request URL: 
interview-assign-ihor-senkiv.herokuapp.com/:8080/api/transactions/123

Example of JSON  response body: 

```sh
{
    "id": 1,
    "txId": "123",
    "user": "2",
    "txAmount": 100,
    "txAmountCy": "EUR",
    "fee": null,
    "feeCy": null,
    "feeMode": null,
    "authCode": "261984c7-10f4-4cb8-856c-768f72b4c675",
    "transactionStatus": "PENDING"
}
```