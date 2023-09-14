package com.iemr.hwc.service.foetalmonitor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.iemr.hwc.utils.exception.IEMRException;

public class FoetalMonitorServiceImplTest {
    private FoetalMonitorServiceImpl foetalMonitorServiceImpl;
    private HttpURLConnection mockConnection;

    @Before
    public void setUp() {
        foetalMonitorServiceImpl = new FoetalMonitorServiceImpl();
        mockConnection = mock(HttpURLConnection.class);

    }

    String result = "";

    @Test
    public void testGeneratePDF() throws Exception {
        // Set up a test InputStream to simulate the HTTP response
        byte[] testPdfData = "test PDF Data".getBytes();
        InputStream testInputStream = new ByteArrayInputStream(testPdfData);
        when(mockConnection.getInputStream()).thenReturn(testInputStream);

        // Set up a test file path
        String testFilePath = "https://github.com/samirparhi-dev/test/raw/main/sampleMedicalReport.pdf";
        // Create a temporary directory for testing
        String tempDir = "/Users/samirranjanparhi/test";
        String foetalMonitorFilePath = tempDir + "/foetalMonitorFiles/";

        // Ensure the directory exists
        Files.createDirectories(Paths.get(foetalMonitorFilePath));

        try {

            // Invoke the generatePDF method
            result = foetalMonitorServiceImpl.generatePDF(testFilePath);
            // Assert that the result is not null
            assertNotNull(result);
            // Assert that the result starts with the foetalMonitorFilePath
            assertTrue(result.startsWith(foetalMonitorFilePath));
            File f = new File(result);
            boolean check = f.exists();
            assertTrue(check);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } finally {
            // Clean up the temporary directory
            Files.delete(Paths.get(result));
        }
        // Verify that the HttpURLConnection was used properly
        System.out.println("Reaching Goal1");
        verify(mockConnection).setRequestMethod("GET");
        System.out.println("Reaching Goal2");
        verify(mockConnection).disconnect();
        System.out.println("Reaching Goal3");
    }

    @Test
    public void testReadPDFANDGetBase64() {
        String sampleFilePath = "/Users/samirranjanparhi/test/ultraSoundReport.pdf";
        try {
            String base64Data = foetalMonitorServiceImpl.readPDFANDGetBase64(sampleFilePath);
            // Assert that the result is not null or empty
            assertNotNull(base64Data);
            assertFalse(base64Data.isEmpty());
        } catch (IEMRException | IOException e) {
            // Handle exceptions as needed, or fail the test if you expect them
            fail("Exception thrown: " + e);
        }
    }
}
