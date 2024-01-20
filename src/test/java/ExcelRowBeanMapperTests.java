import com.poimapper.ExcelRowBeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelRowBeanMapperTests {

    @Test
    public void testRowBeanMapper(){
        LinkedHashMap<String, Map<String,String>> columnMap = new LinkedHashMap<>();
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
//        columnMap.put("someNestedField",Map.of("fieldMapping","nested:val1"));
//        columnMap.put("someNestedField2",Map.of("fieldMapping","nested:val2"));
        ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
        UserInfo userInfo=null;
        try(FileInputStream fis = new FileInputStream("src/test/resources/BeanMapperTestExcel.xlsx")){
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(1);
            userInfo = mapper.fromExcelRow(row,new UserInfo());
            System.out.println(userInfo);

        }catch (IOException e){

        }
        UserInfo expectedUserInfo = new UserInfo(
                "John Cena",
                LocalDate.of(1977, 4, 23),
                1234,
                2.22,
                new BigDecimal("3334444"),
                UserInfo.Gender.MALE,
                UserInfo.UserType.ADMIN,
                null,  // Assuming registrationTime is null
                true,
                new UserInfo.Name("Queen", "Silpa", new UserInfo.Status(false, "test note"))
        );
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }
}
