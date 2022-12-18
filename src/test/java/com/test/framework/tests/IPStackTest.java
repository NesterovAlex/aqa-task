package com.test.framework.tests;

import com.test.framework.api.IPStack;
import com.test.framework.api.model.IpInfo;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.test.framework.api.EndPoints.IP;
import static org.testng.Assert.assertEquals;


public class IPStackTest extends BaseAPITest{

	@BeforeMethod
	public void setUp() {
		IPStack.setBaseUrl();
	}

	@Test
	void assertTheResponseCode() {
		Response response = IPStack.getLocation(IP);

		//Assert the response code;
		assertStatusCode(response, 200);
	}

	@Test
	void assertLatitudeWithTolerance() {
		Response response = IPStack.getLocation(IP);

		//Parse the response
		IpInfo ipInfo = deserializeJSON(response, IpInfo.class);

		//Assert your latitude with a 0.01° tolerance
		double expectedLatitude = 50.26;
		double actualLatitude = ipInfo.getLatitude();
		double tolerance = 0.01;

		assertEquals(actualLatitude, expectedLatitude, tolerance);
	}

	@Test
	void assertLongitudeWithTolerance() {
		Response response = IPStack.getLocation(IP);

		//Parse the response
		IpInfo ipInfo = deserializeJSON(response, IpInfo.class);

		//Assert longitude with a 0.01° tolerance
		double expectedLongitude = 28.66;
		double actualLongitude = ipInfo.getLongitude();
		double tolerance = 0.01;

		assertEquals(actualLongitude, expectedLongitude, tolerance);
	}


}
