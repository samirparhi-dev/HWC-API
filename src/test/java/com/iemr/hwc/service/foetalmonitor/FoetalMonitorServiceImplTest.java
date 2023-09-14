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
            String result = foetalMonitorServiceImpl.generatePDF(testFilePath);
            System.out.println(result);
            // Assert that the result is not null
            assertNotNull(result);
            // Assert that the result starts with the foetalMonitorFilePath
            assertTrue(result.startsWith(foetalMonitorFilePath));
            boolean check = new File(foetalMonitorFilePath, "sampleMedicalReport.pdf").exists();
            assertTrue(check);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e);
        } finally {
            // Clean up the temporary directory
            Files.deleteIfExists(Paths.get(foetalMonitorFilePath));
        }
        // Verify that the HttpURLConnection was used properly
        verify(mockConnection).setRequestMethod("GET");
        verify(mockConnection).disconnect();
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
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
