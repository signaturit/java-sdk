[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=signaturit_java-sdk&metric=alert_status&token=20b737b75f8308736304a2843f66f40a5768e11e)](https://sonarcloud.io/dashboard?id=signaturit_java-sdk)

=========================================
DO NOT USE MASTER BRANCH
========================

Signaturit JAVA SDK
=====================
This package is a JAVA wrapper around the Signaturit API. If you didn't read the documentation yet, maybe it's time to take a look [here](https://docs.signaturit.com/).

You'll need at least JAVA 1.7 to use this package.

Configuration
-------------

The recommended way to install the SDK is through [Maven](https://maven.apache.org/).

Add the dependencies to your pom.xml :
```xml
<dependency>
  <groupId>com.signaturit.api</groupId>
  <artifactId>java-sdk</artifactId>
  <version>1.2.1</version>
</dependency>
```

or [Gradle](http://gradle.org/).

```json
compile 'com.signaturit.api:java-sdk:1.2.1'
```

Then import the library and instantiate the
Client class passing in your API access token.

```java
Client client = new Client("YOUR_ACCESS_TOKEN");
```

Please note that by default the client will use our sandbox API. When you are ready to start using the production environment just get the correct access token and pass an additional argument to the constructor:

```java
Client client = new Client("YOUR_ACCESS_TOKEN", true);
```
All Client's methods will return a object of Response class.

Example: Print body result as String.

```java
Response response = client.countSignatures();
		
System.out.println(response.body().string());
```

Examples
--------

## Signature request

### Get all signature requests

Retrieve all data from your signature requests using different filters.

##### All signatures

```java
response = client.getSignatures();
```

##### Getting the last 50 signatures

```java
response = client.getSignatures(50);
```

##### Getting signatures with custom field "crm_id", limit = 10 and offset = 100

```java
HashMap<String, Object> filters = new HashMap<String,Object>();
filters.put("crm_id", "customId");
response = client.getSignatures(10, 100, filters);
```

### Count signature requests

Count your signature requests.

```java
HashMap<String, Object> filters = new HashMap<String,Object>();
filters.put("crm_id", "customId");
response = client.countSignatures(filters);
```

### Get signature request

Get the information regarding a single signature request passing its ID.

```java
response = client.getSignature("signatureId");
```

###  Signature request

Create a new signature request. You can check all signature [params](https://docs.signaturit.com/api/v3#sign_create_sign).

```java
ArrayList<File> files = new ArrayList<File>();
File file = new File("/documents/contracts/receipt250.pdf");
files.add(file);

ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String,Object>>();
HashMap<String, Object> recipient= new HashMap<String, Object>();

recipient.put("email", "john.doe@example.com");
recipient.put("fullname", "John Doe");
recipients.add(recipient);

HashMap<String, Object> options= new HashMap<String, Object>();
options.put("subject", "Receipt no. 250");
options.put("body", "please sign the receipt");

response = client.createSignature(files, recipients, options);
```

You can send templates with the fields filled
```java
//you can add multiple templates
ArrayList<String> templates = new ArrayList<String>();
templates.add("templateName");
options.put("templates", templates);

response = client.createSignature(null, recipients, options);
```
### Cancel signature request

Cancel a signature request.

```java
response = client.cancelSignature("signatureId");
```

### Send reminder

Send a reminder email.

```java
response = client.sendSignatureReminder("signatureId");
```

### Get audit trail

Get the audit trail of a signature request document and save it locally.

```java
response = client.downloadAuditTrail("signatureId", "documentId");
```

### Get signed document

Get the signed document of a signature request document and save it locally.

```java
response = client.downloadSignedDocument("signatureId", "documentId");
```

## Branding

### Get brandings

Get all account brandings.

```java
response = client.getBrandings();
```

### Get branding

Get a single branding.

```java
response = client.getBranding("brandingId");
```

### Create branding

Create a new branding. You can check all branding [params](https://docs.signaturit.com/api/v3#set_branding).`

```java
HashMap<String, Object> applicationText = new HashMap<String, Object>();
applicationText.put("sign_button", "test sign");
applicationText.put("send_button", "test send");

HashMap<String, Object> options = new HashMap<String, Object>();
options.put("application_texts", applicationText);
options.put("layout_color", "#FFBF00");

response = client.createBranding(options);
```

### Update branding

Update a single branding.

```java
response = $client.updateBranding("brandingId", options);
```

## Template

### Get all templates

Retrieve all (delimited by limit and offset) template data from your templates.

```java
int limit = 100;
int offset = 0;

response = client.getTemplates(limit, offset);
```

## Email

### Get emails

####Get all certified emails

Retrieve all certified emails data.

```java
response = client.getEmails();
```

####Get last 50 emails

```java
int limit = 50;

response = client.getEmails(limit);
```

### Count emails

Count all certified emails.

```java
response = client.countEmails();
```

### Get email

Get a single email

```java
response = client.getEmail('emailId');
```

### Create email

Create a new certified email

```java
ArrayList<File> files = new ArrayList<File>();
File fileToEmail = new File("/path/youPdf.pdf");
files.add(fileToEmail);

client.createEmail(files, recipients, "subject", "body");
```

### Get audit trail document

Get the audit trail document of an email request and save it in the submitted path.

```java
response = client.downloadEmailAuditTrail("emailId","certificateId");
```
