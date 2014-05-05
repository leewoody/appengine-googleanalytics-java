/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.appengine.demos.tracking;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class GoogleAnalyticsTrackingTest {

  private static final String TEST_GA_TRACKING_ID = "UA-XXXX-Y";

  @Test
  public void testTrackEventBasic() throws IOException {
    HTTPResponse mockHTTPResponse = mock(HTTPResponse.class);
    URLFetchService mockUrlFetchService = mock(URLFetchService.class, RETURNS_MOCKS);
    GoogleAnalyticsTracking.setUrlFetchService(mockUrlFetchService);
    GoogleAnalyticsTracking.setGoogleAnalyticsTrackingId(TEST_GA_TRACKING_ID);

    when(mockUrlFetchService.fetch(any(HTTPRequest.class))).thenReturn(mockHTTPResponse);
    when(mockHTTPResponse.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    assertTrue(GoogleAnalyticsTracking
        .trackEventToGoogleAnalytics("Error", "Payment", "Amount", "100"));
  }

  @Test
  public void testTrackEventVerifyParameters() throws IOException {
    ArgumentCaptor<HTTPRequest> httpRequestCaptor = ArgumentCaptor.forClass(HTTPRequest.class);
    HTTPResponse mockHTTPResponse = mock(HTTPResponse.class);
    URLFetchService mockUrlFetchService = mock(URLFetchService.class, RETURNS_MOCKS);
    GoogleAnalyticsTracking.setUrlFetchService(mockUrlFetchService);
    GoogleAnalyticsTracking.setGoogleAnalyticsTrackingId(TEST_GA_TRACKING_ID);

    when(mockUrlFetchService.fetch(httpRequestCaptor.capture())).thenReturn(mockHTTPResponse);
    when(mockHTTPResponse.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    assertTrue(GoogleAnalyticsTracking
        .trackEventToGoogleAnalytics("Error", "Payment", "Amount", "100"));
    String payload = new String(httpRequestCaptor.getValue().getPayload(),
        StandardCharsets.UTF_8.name());
    assertEquals("v=1&tid=UA-XXXX-Y&cid=555&t=event&ec=Error&ea=Payment&el=Amount&ev=100",
        payload);
  }

  @Test
  public void testTrackEventMissingGATrackingid() throws IOException {
    HTTPResponse mockHTTPResponse = mock(HTTPResponse.class);
    URLFetchService mockUrlFetchService = mock(URLFetchService.class, RETURNS_MOCKS);
    GoogleAnalyticsTracking.setUrlFetchService(mockUrlFetchService);

    when(mockUrlFetchService.fetch(any(HTTPRequest.class))).thenReturn(mockHTTPResponse);
    when(mockHTTPResponse.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    try {
      GoogleAnalyticsTracking.trackEventToGoogleAnalytics(null, "Payment", "Amount", "100");
      // Should not get here.
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals("Null value", e.getMessage());
    }
  }

  @Test
  public void testTrackEventMissingCategoryParameter() throws IOException {
    HTTPResponse mockHTTPResponse = mock(HTTPResponse.class);
    URLFetchService mockUrlFetchService = mock(URLFetchService.class, RETURNS_MOCKS);
    GoogleAnalyticsTracking.setUrlFetchService(mockUrlFetchService);
    GoogleAnalyticsTracking.setGoogleAnalyticsTrackingId(TEST_GA_TRACKING_ID);

    when(mockUrlFetchService.fetch(any(HTTPRequest.class))).thenReturn(mockHTTPResponse);
    when(mockHTTPResponse.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    try {
      GoogleAnalyticsTracking.trackEventToGoogleAnalytics(null, "Payment", "Amount", "100");
      // Should not get here.
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Null value", e.getMessage());
    }
  }

  @Test
  public void testTrackEventMissingActionParameter() throws IOException {
    HTTPResponse mockHTTPResponse = mock(HTTPResponse.class);
    URLFetchService mockUrlFetchService = mock(URLFetchService.class, RETURNS_MOCKS);
    GoogleAnalyticsTracking.setUrlFetchService(mockUrlFetchService);
    GoogleAnalyticsTracking.setGoogleAnalyticsTrackingId(TEST_GA_TRACKING_ID);

    when(mockUrlFetchService.fetch(any(HTTPRequest.class))).thenReturn(mockHTTPResponse);
    when(mockHTTPResponse.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

    try {
      GoogleAnalyticsTracking.trackEventToGoogleAnalytics("Error", null, "Amount", "100");
      // Should not get here.
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Null value", e.getMessage());
    }
  }
}
