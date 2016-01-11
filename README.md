============================================================
DO NOT USE THIS CODE ON PRODUCTION UNTIL NEW RELEASE IS DONE
============================================================

Signaturit JAVA SDK
=====================
This package is a JAVA wrapper around the Signaturit API. If you didn't read the documentation yet, maybe it's time to take a look [here](http://docs.signaturit.com/).

You'll need at least JAVA 1.7 to use this package.

Configuration
-------------

The recommended way to install the SDK is through [Maven](https://maven.apache.org/).

Add the dependencies to you pom.xml :
```xml
		<dependency>
			<groupId>com.signaturit.java_sdk</groupId>
			<artifactId>java-sdk</artifactId>
			<version>1.0.0</version>
		</dependency>
```
Then import the library and instantiate the
Client class passing in your API access token.

```java
import com.signaturit.java_sdk.client;
Client client = new Client("Bearer {youKey}", true);
```

Please note that by default the client will use our sandbox API. When you are
ready to start using the production environment just get the correct access token and pass an additional argument to the constructor:

Examples
--------

## Signature request

### Get all signature requests

Retrieve all data from your signature requests using different filters.

##### All signatures

```java
response = client.getSignatures(null, null, null);
```

##### Getting the last 50 signatures

```java
response = client.getSignatures(50, 0, null);
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
response = client.countSignatures(filters);
```

### Get signature request

Get the information regarding a single signature request passing its ID.

```java
response = client.getSignature("signatureId");
```

###  Signature request

Create a new signature request. Check all available [options](http://docs.signaturit.com/api/#sign_create_sign).

```java
String filePath = "/documents/contracts/receipt250.pdf";
ArrayList<HashMap<String, Object>> recipients = new ArrayList<HashMap<String,Object>>();
HashMap<String, Object> recipient= new HashMap<String, Object>();
HashMap<String, Object> options= new HashMap<String, Object>();

recipient.put("email", "john.doe@example.com");
recipient.put("fullname", "John Doe");
recipients.add(recipient);

options.put("subject", "Receipt no. 250");
options.put("body", "please sign the receipt");

response = client.createSignature(filePath, recipients, options);
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
response = client.sendSignatureReminder("signatureId", "documentId");
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

## Account

### Get account

Retrieve the information of your account.

```java
response = client.getAccount();
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

Create a new branding. You can check all branding params [here](http://docs.signaturit.com/api/#set_branding).`

```java

HashMap<String, Object> options = new HashMap<String, Object>();
HashMap<String, Object> applicationText = new HashMap<String, Object>();

applicationText.put("sign_button", "test sign");
applicationText.put("send_button", "test send");
options.put("application_texts", applicationText);
options.put("corporate_layout_color", "#FFBF00");

response = client.createBranding(options);
```

### Update branding

Update a single branding.

```java
response = $client.updateBranding("brandingId", options);
```

### Update branding email

Change a email. Learn more about the emails [here](http://docs.signaturit.com/api/#put_template_branding).

```java
String filePath = "/html/youHTML.html";
String templateName = "sign_request";

response = client.updateBrandingEmail("brandingId", templateName, filePath);
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

Retrieve all (delimited by limit and offset and filters) certified emails data.
Is possible to navigate usign limit and ofsset and send filters.

```java
response = client.getEmails(limit, offset, options)
```

### Get email

Get a single email

```java
response = client.getEmail('emailId');
```

### Count emails

Count all certified emails. Send null for no filters.

```java
response = client.countEmails(filters);
```

### Create email

Create a new certified email. Put null instead of options for no options

```java
ArrayList<String> filesToEmail = new ArrayList<String>();
filesToEmail.add("/path/youPdf.pdf");
client.createEmail(filesToEmail, recipients, "subject", "body", options);

```

### Get audit trail document

Get the audit trail document of an email request and save it in the submitted path.

```java
response = client.downloadEmailAuditTrail("emailId","certificateId");
```
