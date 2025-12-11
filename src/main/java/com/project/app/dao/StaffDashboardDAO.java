package main.java.com.project.app.dao;

import main.java.com.project.app.model.LogAktivitas;
import main.java.com.project.app.model.StatistikDokumen;

import java.util.List;
import java.util.Map;

public interface StaffDashboardDAO {
    List<LogAktivitas> getAktivitasUser(int userId, int limit) throws Exception;
    List<StatistikDokumen> getDokumenBulanan(int userId, int month, int year) throws Exception;
    Map<String, Integer> getTotalDokumenSaya(int userId) throws Exception;
    Map<String, Integer> getTagihanStatusCounts(int userId) throws Exception;
    List<LogAktivitas> searchAktivitas(int userId, String keyword, int limit) throws Exception;
}
