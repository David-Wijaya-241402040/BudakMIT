package main.java.com.project.app.dao;

import main.java.com.project.app.model.*;

import java.util.List;

public interface DashboardDAO {
    // legacy (bulan berjalan)
    int countSPThisMonth();
    int countTagihanPendingThisMonth();
    int countTagihanLunasThisMonth();
    int countTagihanBatalThisMonth();
    List<UserSummary> getStaffSummariesThisMonth();
    List<MonthlyDocumentDetail> getMonthlyDocumentDetails();
    StatusSummary getStatusSummaryThisMonth();

    // new: range by year-month (inclusive). Format param: "YYYY-MM"
    int countSPBetween(String startYearMonth, String endYearMonth);
    int countTagihanPendingBetween(String startYearMonth, String endYearMonth);
    int countTagihanLunasBetween(String startYearMonth, String endYearMonth);
    int countTagihanBatalBetween(String startYearMonth, String endYearMonth);
    List<UserSummary> getStaffSummariesBetween(String startYearMonth, String endYearMonth);
    List<MonthlyDocumentDetail> getMonthlyDocumentDetailsBetween(String startYearMonth, String endYearMonth);
    StatusSummary getStatusSummaryBetween(String startYearMonth, String endYearMonth);
}
