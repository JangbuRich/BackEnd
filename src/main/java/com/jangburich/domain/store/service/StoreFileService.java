package com.jangburich.domain.store.service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.domain.StoreTeam;
import com.jangburich.domain.store.presentation.dto.response.store.StoreChargeHistoryResponse;
import com.jangburich.domain.store.presentation.dto.response.store.StoreTeamResponseDTO;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.store.repository.StoreTeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;

import lombok.RequiredArgsConstructor;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreFileService {
    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final PointTransactionRepository pointTransactionRepository;

    public byte[] createExcel(String userId, Integer period) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(LocalDate.now().minusMonths(period) + "~" + LocalDate.now());

        CellStyle rightCellStyle = sheet.getWorkbook().createCellStyle();
        rightCellStyle.setWrapText(true);
        rightCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        // 헤더 행 생성
        Row headerRow = sheet.createRow(0);

        // 헤더 스타일 생성
        XSSFCellStyle headerStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();

        // 헥사 색상 설정
        XSSFColor customColor = new XSSFColor(Color.decode("#FF7048"), null);

        headerStyle.setFillForegroundColor(customColor); // 배경색 설정
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 패턴 설정
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // 가로 가운데 정렬
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬

        headerStyle.setBorderTop(BorderStyle.THIN); // 위쪽 테두리
        headerStyle.setBorderBottom(BorderStyle.THIN); // 아래쪽 테두리
        headerStyle.setBorderLeft(BorderStyle.THIN); // 왼쪽 테두리
        headerStyle.setBorderRight(BorderStyle.THIN); // 오른쪽 테두리
        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 위쪽 테두리 색상
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 아래쪽 테두리 색상
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 왼쪽 테두리 색상
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 오른쪽 테두리 색상

        // 헤더 글꼴 스타일 설정
        org.apache.poi.ss.usermodel.Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true); // 볼드체
        headerFont.setColor(IndexedColors.WHITE.getIndex()); // 글자 색상
        headerStyle.setFont(headerFont);

        // 헤더 내용 배열로 관리
        String[] headers = {
            "① 선결제일자",
            "② 그룹명",
            "③ 선결제액",
            "④ 부가세 (③ * 1/11)",
            "⑤ 공급가액 (③-④)",
            "⑥ 누적선결제건수",
            "⑦ 누적선결제액",
            "⑧ 누적차감건수",
            "⑨ 누적차감금액",
            "⑩ 잔여선결제액 (⑦-⑨)"
        };

        // 헤더 작성
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i + 1); // 셀 생성 (i + 1로 시작)
            cell.setCellValue(headers[i]); // 헤더 텍스트 설정
            cell.setCellStyle(headerStyle); // 스타일 적용
        }

        // 행 높이 설정 (헤더 텍스트가 잘 보이도록)
        headerRow.setHeightInPoints(20); // 원하는 높이로 설정

        java.util.List<StoreChargeHistoryResponse> prepayHistoryResponses = pointTransactionRepository.findAllByStore(store)
            .stream()
            .filter(storeChargeHistoryResponse ->
                storeChargeHistoryResponse.transactionType() == TransactionType.PREPAY
                    && storeChargeHistoryResponse.createdAt().isAfter(LocalDateTime.now().minusMonths(period))
            )
            .sorted(Comparator.comparing(StoreChargeHistoryResponse::createdAt)) // 오름차순 정렬
            .toList();

        List<StoreChargeHistoryResponse> foodPurchaseHistoryResponses = pointTransactionRepository.findAllByStore(store)
            .stream()
            .filter(
                storeChargeHistoryResponse -> storeChargeHistoryResponse.transactionType()
                    == TransactionType.FOOD_PURCHASE)
            .toList();

        Integer totalPrePay = 0;

        int rowIndex = 1;
        for (StoreChargeHistoryResponse prepayHistoryResponse : prepayHistoryResponses) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(1)
                .setCellValue(prepayHistoryResponse.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dataRow.createCell(2).setCellValue(prepayHistoryResponse.teamName());
            totalPrePay += prepayHistoryResponse.transactionedPoint();
            dataRow.createCell(3).setCellValue(prepayHistoryResponse.transactionedPoint());
            int surtax = prepayHistoryResponse.transactionedPoint() / 11;
            dataRow.createCell(4).setCellValue(surtax);
            dataRow.createCell(5).setCellValue(prepayHistoryResponse.transactionedPoint() - surtax);
            StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(store.getId(),
                    prepayHistoryResponse.teamId())
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_CHECK));
            dataRow.createCell(6).setCellValue(storeTeam.getPrepayCount());
            dataRow.createCell(7).setCellValue(storeTeam.getPoint()); // 누적 선결제 금액
            dataRow.createCell(8)
                .setCellValue(foodPurchaseHistoryResponses.stream().filter(storeChargeHistoryResponse -> Objects.equals(
                    storeChargeHistoryResponse.teamId(), prepayHistoryResponse.teamId())).count());
            dataRow.createCell(9).setCellValue(storeTeam.getPoint() - storeTeam.getRemainPoint());
            dataRow.createCell(10).setCellValue(storeTeam.getRemainPoint());
        }
        rowIndex += 2;

        Row dataRow = sheet.createRow(rowIndex++);

        Cell cell = dataRow.createCell(3);
        cell.setCellValue("기간");
        cell.setCellStyle(headerStyle);

        Cell periodCell = dataRow.createCell(4);
        periodCell.setCellValue(LocalDate.now().minusMonths(period) + " ~\n " + LocalDate.now());
        periodCell.setCellStyle(rightCellStyle);
        dataRow.setHeightInPoints((short) (dataRow.getHeightInPoints() * 2));

        dataRow = sheet.createRow(rowIndex++);
        cell = dataRow.createCell(3);
        cell.setCellValue("① 총 선 결제액(매출)");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(totalPrePay);
        periodCell.setCellStyle(rightCellStyle);

        dataRow = sheet.createRow(rowIndex++);
        cell = dataRow.createCell(3);
        cell.setCellValue("② 부가세 총액 (① * 1/11)");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(totalPrePay / 11);
        periodCell.setCellStyle(rightCellStyle);

        dataRow = sheet.createRow(rowIndex++);
        cell = dataRow.createCell(3);
        cell.setCellValue("③ 총 공급가액 (①-②)");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(totalPrePay - totalPrePay / 11);
        periodCell.setCellStyle(rightCellStyle);

        int totalPoint = storeTeamRepository.findAllByStore(store)
            .stream()
            .mapToInt(StoreTeamResponseDTO::point)
            .sum();

        int remainPoint = storeTeamRepository.findAllByStore(store)
            .stream()
            .mapToInt(StoreTeamResponseDTO::remainPoint)
            .sum();

        dataRow = sheet.createRow(rowIndex++);
        cell = dataRow.createCell(3);
        cell.setCellValue("④ 총 누적 선 결제액");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(totalPoint);
        periodCell.setCellStyle(rightCellStyle);

        dataRow = sheet.createRow(rowIndex++);
        cell = dataRow.createCell(3);
        cell.setCellValue("⑤ 총 누적 차감액");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(totalPoint - remainPoint);
        periodCell.setCellStyle(rightCellStyle);

        dataRow = sheet.createRow(rowIndex);
        cell = dataRow.createCell(3);
        cell.setCellValue("⑥ 총 잔여 선 결제액 (④-⑤)");
        cell.setCellStyle(headerStyle);

        periodCell = dataRow.createCell(4);
        periodCell.setCellValue(remainPoint);
        periodCell.setCellStyle(rightCellStyle);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (currentWidth * 1.2));
        }

        // 엑셀 파일을 바이트 배열로 변환
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류가 발생했습니다.", e);
        } finally {
            try {
                workbook.close(); // close 중 발생한 예외 처리
            } catch (IOException e) {
                System.err.println("Workbook 닫는 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
