package com.smartparking.api.controller;

import com.smartparking.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/occupancy")
    public ResponseEntity<byte[]> generateOccupancyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer parkingLotId,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] report = reportService.generateOccupancyReport(start, end, parkingLotId, format);
        return createReportResponse(report, "occupancy_report", format);
    }

    @GetMapping("/revenue")
    public ResponseEntity<byte[]> generateRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] report = reportService.generateRevenueReport(start, end, paymentMethod, format);
        return createReportResponse(report, "revenue_report", format);
    }

    @GetMapping("/vehicle-usage")
    public ResponseEntity<byte[]> generateVehicleUsageReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] report = reportService.generateVehicleUsageReport(start, end, vehicleType, format);
        return createReportResponse(report, "vehicle_usage_report", format);
    }

    @GetMapping("/peak-hours")
    public ResponseEntity<byte[]> generatePeakHoursReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Integer parkingLotId,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] report = reportService.generatePeakHoursReport(start, end, parkingLotId, format);
        return createReportResponse(report, "peak_hours_report", format);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<byte[]> generateMonthlySummaryReport(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] report = reportService.generateMonthlySummaryReport(year, month, format);
        return createReportResponse(report, "monthly_summary_report", format);
    }

    private ResponseEntity<byte[]> createReportResponse(byte[] report, String reportName, String format) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType;
        String filename;

        switch (format.toUpperCase()) {
            case "PDF":
                mediaType = MediaType.APPLICATION_PDF;
                filename = reportName + ".pdf";
                break;
            case "EXCEL":
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
                filename = reportName + ".xlsx";
                break;
            case "CSV":
                mediaType = MediaType.parseMediaType("text/csv");
                filename = reportName + ".csv";
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }

        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(report);
    }
} 