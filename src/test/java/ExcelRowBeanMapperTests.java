import com.poimapper.ExcelRowBeanMapper;
import com.poimapper.config.PoiBuilderConfig;
import com.poimapper.config.PoiConfig;
import com.poimapper.exception.ExcelRowBeanMapperException;
import com.poimapper.util.ExcelSheetGeneratorUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class ExcelRowBeanMapperTests {
    private static Workbook workbook;
    private static Sheet sheet;
    private UserInfo expectedUserInfo = new UserInfo(
            "John Cena",
            LocalDate.of(1977, 4, 23),
            1234,
            2.22,
            new BigDecimal("3334444"),
            UserInfo.Gender.MALE,
            UserInfo.UserType.ADMIN,
            LocalDateTime.of(LocalDate.of(2022, 01, 01), LocalTime.of(15, 30, 45)),  // Assuming registrationTime is null
            true,
            new UserInfo.Name("Queen", "Silpa", new UserInfo.Status(false, "test note"))
    );
    private LinkedHashMap<String, PoiConfig> columnMap = new LinkedHashMap<>();

    @BeforeClass
    public static void setUp() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/BeanMapperTestExcel.xlsx")) {
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
            }
        }
    }

    private void setFirstRowMapping() {
        columnMap = new LinkedHashMap<>();
        columnMap.put("name", new PoiConfig("name"));
        columnMap.put("birthDate", new PoiConfig("birthDate"));
        columnMap.put("phoneNumber", new PoiConfig("phone"));
        columnMap.put("heightInMeters", new PoiConfig("heightInMeters"));
        columnMap.put("accountBalance", new PoiConfig("amountBalance"));
        columnMap.put("gender", new PoiConfig("gender"));
        columnMap.put("userType", new PoiConfig("userType"));
        columnMap.put("registrationTime", new PoiConfig("registrationTime"));
        columnMap.put("isActive", new PoiConfig("isActive"));
        columnMap.put("firstName", new PoiConfig("motherInfo:firstName"));
        columnMap.put("lastName", new PoiConfig("motherInfo:lastName"));
        columnMap.put("healthy", new PoiConfig("motherInfo:status:healthy"));
        columnMap.put("note", new PoiConfig("motherInfo:status:note"));
    }

    private void setSecondRowMapping() {
        columnMap = new LinkedHashMap<>();
        columnMap.put("Account Balance", new PoiConfig("amountBalance", null, new BigDecimal(3334444)));
        columnMap.put("Phone Number", new PoiConfig("phone"));
        columnMap.put("BirthDate", new PoiConfig("birthDate", "dd/MM/yyyy"));
        columnMap.put("Gender", new PoiConfig("gender"));
        columnMap.put("Name", new PoiConfig("name"));
        columnMap.put("Height In Meters", new PoiConfig("heightInMeters"));
        columnMap.put("User Type", new PoiConfig("userType"));
        columnMap.put("Is Active", new PoiConfig("isActive"));
        columnMap.put("Registration Time", new PoiConfig("registrationTime", "dd/yyyy/MM HH:mm:ss"));
        columnMap.put("First Name", new PoiConfig("motherInfo:firstName"));
        columnMap.put("Last kName", new PoiConfig("motherInfo:lastName"));
        columnMap.put("Healthy", new PoiConfig("motherInfo:status:healthy"));
        columnMap.put("Note", new PoiConfig("motherInfo:status:note"));
    }

    @Test
    public void testForValueHandleShouldThrowException() {

        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(3);
        // Sub Test 1
        columnMap = new LinkedHashMap<>();
        columnMap.put("Account Balance", new PoiConfig("ammoountBalance"));
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .setMapperSettings(new PoiBuilderConfig(false, true))
                .build();
        Throwable throwable = assertThrows(Exception.class, () -> mapper.fromExcelRow(row, new UserInfo()));
        assertEquals("[VALUE_HANDLE_EXCEPTION] : An error occurred while attempting to handle the value from field \"ammoountBalance\" .", throwable.getMessage());
    }

    @Test
    public void testForValueConversionShouldThrowException() {
        Row row = sheet.getRow(3);
        // Sub Test 1
        columnMap = new LinkedHashMap<>();
        columnMap.put("Account Balance", new PoiConfig("birthDate"));
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .setMapperSettings(new PoiBuilderConfig(false, true))
                .build();
        Throwable throwable = assertThrows(Exception.class, () -> mapper.fromExcelRow(row, new UserInfo()));
        assertEquals("[VALUE_CONVERSION_FAILED] : For field \"birthDate\", Failed to cast value \"3334444\" from String type to \"class java.time.LocalDate", throwable.getMessage());
    }

    @Test(expected = ExcelRowBeanMapperException.class)
    public void testFromNullRowShouldThrowException() {
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .build();
        mapper.fromExcelRow(null, new UserInfo());
    }

    @Test
    public void testUserInfoForFirstRow() {
        setFirstRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .setMapperSettings(null)
                .build();
        Row row = sheet.getRow(1);
        UserInfo userInfo = mapper.fromExcelRow(row, new UserInfo());
        assertEquals(userInfo, expectedUserInfo);
    }

    @Test
    public void testUserInfoForSecondRow() {
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .build();
        Row row = sheet.getRow(3);
        UserInfo userInfo = mapper.fromExcelRow(row, new UserInfo());
        assertEquals(userInfo, expectedUserInfo);
    }

    @Test
    public void testUserInfoForMultipleRows() {
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder()
                .setRowMapping(columnMap)
                .build();
        for (int i = 4; i < 14; i++) {
            Row row = sheet.getRow(i);
            UserInfo userInfo = mapper.fromExcelRow(row, new UserInfo());
            UserInfo expectedUser = UserData10Rows.userInfos.get(i - 4);
            assertEquals(userInfo, expectedUser);
        }
    }

    @Test
    public void testGenerateExcelFileWithRowMapping() {
        setSecondRowMapping();
        ExcelSheetGeneratorUtil excelSheetGenerator = new ExcelSheetGeneratorUtil
                .Builder()
                .setRowMapping(columnMap)
                .setPath("src/test/resources/generated")
                .build();
        Boolean isSuccess = excelSheetGenerator.generate("GeneratedExcel");
        assertEquals(isSuccess, true);
    }
}
