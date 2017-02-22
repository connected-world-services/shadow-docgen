package com.connectedworldservices.docgen.rest;

public class MockResponses {

    public final static String WITHOUT_STATE_SERVICE = "{\n" +
            "  \"_messageId\": \"c2eaa60818112a0df03a4bb5bd10f9d8\",\n" +
            "  \"_links\": {\n" +
            "    \"service\": [\n" +
            "      {\n" +
            "        \"name\": \"new-connection\",\n" +
            "        \"href\": \"http://localhost:8089/api/new-connection\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"new\": [\n" +
            "      {\n" +
            "        \"href\": \"http://localhost:8089/api/new-connection\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"self\": [\n" +
            "      {\n" +
            "        \"href\": \"http://localhost:8089/api\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"glossary\": [\n" +
            "      {\n" +
            "        \"name\": \"key-translations\",\n" +
            "        \"title\": \"key-translations-title-code\",\n" +
            "        \"href\": \"http://localhost:8089/api/translations\",\n" +
            "        \"profile\": \"#/_locale-pointer\",\n" +
            "        \"templated\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_locale-pointer\": {\n" +
            "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
            "    \"type\": \"object\",\n" +
            "    \"format\": \"locale-code\",\n" +
            "    \"title\": \"key.TRANSLATIONS\",\n" +
            "    \"additionalProperties\": false,\n" +
            "    \"properties\": {\n" +
            "      \"locale\": {\n" +
            "        \"pattern\": \"^[a-z][a-z]-[a-z][a-z]$\",\n" +
            "        \"description\": \"en-gb\",\n" +
            "        \"type\": \"string\",\n" +
            "        \"title\": \"key.TRANSLATIONS.LOCALE\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public final static String WITH_STATE_SERVICE = "{\n" +
            "  \"_messageId\": \"c2eaa60818112a0df03a4bb5bd10f9d8\",\n" +
            "  \"_links\": {\n" +
            "    \"service\": [\n" +
            "      {\n" +
            "        \"name\": \"new-connection\",\n" +
            "        \"href\": \"http://localhost:8089/api/new-connection\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"application-state\",\n" +
            "        \"title\": \"application-state\",\n" +
            "        \"href\": \"http://localhost:8089/api/state?cws-ref={cws-ref}\",\n" +
            "        \"profile\": \"#/_cws-ref-pointer\",\n" +
            "        \"templated\": true\n" +
            "      }\n" +
            "    ],\n" +
            "    \"new\": [\n" +
            "      {\n" +
            "        \"href\": \"http://localhost:8089/api/new-connection\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"self\": [\n" +
            "      {\n" +
            "        \"href\": \"http://localhost:8089/api\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"glossary\": [\n" +
            "      {\n" +
            "        \"name\": \"key-translations\",\n" +
            "        \"title\": \"key-translations-title-code\",\n" +
            "        \"href\": \"http://localhost:8089/api/translations\",\n" +
            "        \"profile\": \"#/_locale-pointer\",\n" +
            "        \"templated\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_cws-ref-pointer\": {\n" +
            "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
            "    \"type\": \"object\",\n" +
            "    \"format\": \"cws-ref-code\",\n" +
            "    \"additionalProperties\": false,\n" +
            "    \"properties\": {\n" +
            "      \"cws-ref\": {\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"_locale-pointer\": {\n" +
            "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
            "    \"type\": \"object\",\n" +
            "    \"format\": \"locale-code\",\n" +
            "    \"title\": \"key.TRANSLATIONS\",\n" +
            "    \"additionalProperties\": false,\n" +
            "    \"properties\": {\n" +
            "      \"locale\": {\n" +
            "        \"pattern\": \"^[a-z][a-z]-[a-z][a-z]$\",\n" +
            "        \"description\": \"en-gb\",\n" +
            "        \"type\": \"string\",\n" +
            "        \"title\": \"key.TRANSLATIONS.LOCALE\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    public static final String STATE_DATA = "{\n" +
            "  \"account\": {\n" +
            "    \"subscriber\": {\n" +
            "      \"currentDevice\": {\n" +
            "        \"currentOffering\": {\n" +
            "          \"purchasePrice\": {\n" +
            "            \"amount\": 32,\n" +
            "            \"currencyCode\": \"USD\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"createTimestamp\": 1466504277374,\n" +
            "  \"cws-ref\": \"123\",\n" +
            "  \"updateTimestamp\": 1466504277374\n" +
            "}";
}
