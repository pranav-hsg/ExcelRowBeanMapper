
# Poimapper

It is a lightweight library designed to convert Excel rows into Java beans, offering the additional capability to generate Excel sheets based on specified mappings


## Why This Library

This library streamlines the mapping of Excel rows to Java beans by introducing a columnMap mechanism, eliminating the need for manual index adjustments when altering the Excel column order. Unlike traditional methods that require meticulous index updates, the columnMap relies on a LinkedHashMap to maintain the specified order of columns.

Consider a scenario where a new column 'Address' needs to be added before the existing 2nd column. In the traditional approach, manual index updates are necessary. However, with this library, you can effortlessly group related columns in the columnMap, avoiding manual adjustments.

```java
// Traditional method with manual index updates
Cell cell0 = row.getCell(0);
String name = cell0.getStringCellValue();
Cell cell1 = row.getCell(1); // Manually updated index
String birthDate = cell1.getStringCellValue();
.
.
.
Cell cell100 = row.getCell(100);
String amount = cell2.getStringCellValue();
```
To
```
Cell cell0 = row.getCell(0);
String name = cell0.getStringCellValue();
Cell cell1 = row.getCell(1); 
String address = cell1.getStringCellValue();
Cell cell2 = row.getCell(2); // Manually updated index
String address = cell1.getStringCellValue();
.
.
.
Cell cell101 = row.getCell(101); // Manually updated index all the way upto last column
String amount = cell2.getStringCellValue();

```
With the poimapper, the columnMap specifies the order of columns, providing a more flexible and maintainable solution:

```java
columnMap.put("Name", Map.of("fieldMapping", "name", "defaultValue", "Beta"));
columnMap.put("Address", Map.of("fieldMapping", "address"));
columnMap.put("BirthDate", Map.of("fieldMapping", "birthDate", "pattern", "yyyy-MM-dd"));
.
.
.
columnMap.put("Amount", Map.of("fieldMapping", "amount:value"));
```
This approach simplifies the mapping process and enhances flexibility. Additionally, the library allows the generation of Excel sheets directly from the columnMap, offering a comprehensive solution for diverse Excel structures.

## Usage

### To convert excel row to java object

```java

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.poimapper.ExcelRowBeanMapper;
import java.util.LinkedHashMap; 
import java.util.Map;

// Declare the column mapping.
LinkedHashMap<String, Map<String, Object>> columnMap = new LinkedHashMap<>();
// Default value is used when cell is empty
columnMap.put("Name", Map.of("fieldMapping", "name", "defaultValue", "Beta"));
// Pattern is used to override default date format
columnMap.put("BirthDate", Map.of("fieldMapping", "birthDate", "pattern", "yyyy-MM-dd"));
// Colon is used in field mapping is used to indicate nested field
columnMap.put("Amount", Map.of("fieldMapping", "amount:value"));

ExcelRowBeanMapper mapper = new ExcelRowBeanMapper.Builder().setRowMapping(columnMap).build();
Sheet sheet = workbook.getSheetAt(1);
for (int i = 1; i < sheet.getLastRowNum(); i++) {
    Row row = sheet.getRow(i);
    SimpleTestDto userInfo = mapper.fromExcelRow(row, new SimpleTestDto());
    System.out.println(userInfo);
}
```

```java
public class SimpleTestDto {
    private String name;
    private LocalDate birthDate;
    private Amount amount;
    public static class Amount {
        private BigDecimal value;
    }
}
```

### To generate excel sheet from excel mapping


```java
import com.poimapper.util.ExcelSheetGeneratorUtil;

ExcelSheetGeneratorUtil excelSheetGenerator = new ExcelSheetGeneratorUtil
                .Builder()
                .setRowMapping(columnMap)
                .setPath("src/test/resources/generated")
                .build();
Boolean isSuccess = excelSheetGenerator.generate("GeneratedExcel");
```
## License

[MIT](https://choosealicense.com/licenses/mit/)

