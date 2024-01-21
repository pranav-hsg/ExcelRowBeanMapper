import com.poimapper.ExcelRowBeanMapper;
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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@RunWith(JUnit4.class)
public class ExcelRowBeanMapperTests {
    private static Workbook workbook;
    private UserInfo expectedUserInfo = new UserInfo(
            "John Cena",
            LocalDate.of(1977, 4, 23),
            1234,
            2.22,
            new BigDecimal("3334444"),
            UserInfo.Gender.MALE,
            UserInfo.UserType.ADMIN,
            LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(15,30,45)),  // Assuming registrationTime is null
            true,
            new UserInfo.Name("Queen", "Silpa", new UserInfo.Status(false, "test note"))
    );
    private LinkedHashMap<String, Map<String,Object>> columnMap = new LinkedHashMap<>();

    private void setFirstRowMapping(){
        columnMap = new LinkedHashMap<>();
        columnMap.put("name",Map.of("fieldMapping","name"));
        columnMap.put("birthDate",Map.of("fieldMapping","birthDate"));
        columnMap.put("phoneNumber",Map.of("fieldMapping","phone"));
        columnMap.put("heightInMeters",Map.of("fieldMapping","heightInMeters"));
        columnMap.put("accountBalance",Map.of("fieldMapping","amountBalance"));
        columnMap.put("gender",Map.of("fieldMapping","gender"));
        columnMap.put("userType",Map.of("fieldMapping","userType"));
        columnMap.put("registrationTime",Map.of("fieldMapping","registrationTime"));
        columnMap.put("isActive",Map.of("fieldMapping","isActive"));
        columnMap.put("firstName",Map.of("fieldMapping","motherInfo:firstName"));
        columnMap.put("lastName",Map.of("fieldMapping","motherInfo:lastName"));
        columnMap.put("healthy",Map.of("fieldMapping","motherInfo:status:healthy"));
        columnMap.put("note",Map.of("fieldMapping","motherInfo:status:note"));
    }
    private void setSecondRowMapping(){
        columnMap = new LinkedHashMap<>();
        columnMap.put("accountBalance",Map.of("fieldMapping","amountBalance","defaultValue",new BigDecimal(3334444)));
        columnMap.put("phoneNumber",Map.of("fieldMapping","phone"));
        columnMap.put("birthDate",Map.of("fieldMapping","birthDate","pattern","dd/MM/yyyy"));
        columnMap.put("gender",Map.of("fieldMapping","gender"));
        columnMap.put("name",Map.of("fieldMapping","name"));
        columnMap.put("heightInMeters",Map.of("fieldMapping","heightInMeters"));
        columnMap.put("userType",Map.of("fieldMapping","userType"));
        columnMap.put("isActive",Map.of("fieldMapping","isActive"));
        columnMap.put("registrationTime",Map.of("fieldMapping","registrationTime","pattern","dd/yyyy/MM HH:mm:ss"));
        columnMap.put("firstName",Map.of("fieldMapping","motherInfo:firstName"));
        columnMap.put("lastName",Map.of("fieldMapping","motherInfo:lastName"));
        columnMap.put("healthy",Map.of("fieldMapping","motherInfo:status:healthy"));
        columnMap.put("note",Map.of("fieldMapping","motherInfo:status:note"));
    }
    @BeforeClass
    public static void setUp() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/BeanMapperTestExcel.xlsx")) {
            workbook = WorkbookFactory.create(fis);
        } catch (IOException e) {
        }
    }

    @Test(expected = ExcelRowBeanMapperException.class)
    public void testByGeneratingEmptyRowException(){
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(3);
        UserInfo userInfo = mapper.fromExcelRow(null,new UserInfo());
    }
    @Test
    public void testRowBeanMapper(){
        setFirstRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        UserInfo userInfo = mapper.fromExcelRow(row,new UserInfo());
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }
    @Test
    public void testSecondRowBeanMapper(){
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(3);
        UserInfo userInfo = mapper.fromExcelRow(row,new UserInfo());
//        System.out.println(userInfo);
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }
    @Test
    public void test10RowsForBeanMapper(){
        setSecondRowMapping();
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
        Sheet sheet = workbook.getSheetAt(0);
        for(int i=4;i<14;i++){
            Row row = sheet.getRow(i);
            UserInfo userInfo = mapper.fromExcelRow(row,new UserInfo());
            UserInfo expectedUser  = UserData10Rows.userInfos.get(i-4);
            assertThat(userInfo).isEqualTo(expectedUser);
        }
    }

    @Test
    public void testByGeneratingExcelFile(){
        setSecondRowMapping();
        ExcelSheetGeneratorUtil excelSheetGenerator = new ExcelSheetGeneratorUtil
                .Builder()
                .setRowMapping(columnMap)
                .setPath("src/test/resources/generated")
                .build();
        Boolean isSuccess = excelSheetGenerator.generate("GeneratedExcel");
//        System.out.println(isSuccess);
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
}
