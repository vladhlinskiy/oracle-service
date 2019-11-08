/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.oracle;

import com.google.common.collect.Iterators;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Oracle Service Cloud client.
 */
public class OracleServiceCloudClient implements Closeable {
  private static final JsonParser PARSER = new JsonParser();
  private final OracleConfig config;
  private final String serviceRoot;
  private CloseableHttpClient httpClient;

  public OracleServiceCloudClient(OracleConfig config) {
    this.config = config;
    this.serviceRoot = String.format("%s/%s", config.getServerUrl(), OracleConstants.API_ROOT_PATH);
  }

  public Iterator<JsonObject> collection(OracleObject oracleObject) throws IOException {
    CloseableHttpResponse response = httpGet(serviceRoot + oracleObject.getResourceName());
    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    JsonArray collection = PARSER.parse(responseString).getAsJsonObject().getAsJsonArray("items"); // TODO

    return Iterators.transform(collection.<JsonElement>iterator(), JsonElement::getAsJsonObject);
  }

  /**
   * Executes HTTP GET request using specified URI.
   *
   * @param uri URI of resource
   * @return a response object
   * @throws IOException in case of a problem or the connection was aborted
   */
  private CloseableHttpResponse httpGet(String uri) throws IOException {
    // lazy init. So we are able to initialize the class for different checks during validations etc.
    if (httpClient == null) {
      httpClient = createHttpClient();
    }

    HttpGet request = new HttpGet(URI.create(uri));
    return httpClient.execute(request);
  }

  private CloseableHttpClient createHttpClient() throws IOException {
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    // TODO ssl
    // TODO timeouts
    // basic auth
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    if (config.getAuthenticationType() == AuthenticationType.BASIC) {
      AuthScope authScope = new AuthScope(HttpHost.create(config.getServerUrl()));
      credentialsProvider.setCredentials(authScope,
                                         new UsernamePasswordCredentials(config.getUser(), config.getPassword()));
    }
    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    // OAuth 2
    ArrayList<Header> clientHeaders = new ArrayList<>();
    if (config.getAuthenticationType() == AuthenticationType.OAUTH) {
      clientHeaders.add(new BasicHeader("Authorization", "Bearer " + config.getAccessToken()));
    }
    httpClientBuilder.setDefaultHeaders(clientHeaders);

    return httpClientBuilder.build();
  }

  @Override
  public void close() throws IOException {
    if (httpClient != null) {
      httpClient.close();
    }
  }
}
