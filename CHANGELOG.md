# Changelog

## [2.0.0] - 2024-01-27

### Breaking Changes
- **Options Handling:** The way config are provided has been revised for improved configurability. Previously, options were passed as a Map, but now they are accepted using `PoiConfig` and `PoiBuilderConfig`.
    - Ensure to update your code to use the new config handling mechanism.
```java
// Before
columnMap.put("Account Balance",Map.of("fieldMapping","amountBalance","defaultValue",new BigDecimal(3334444)));
// After
columnMap.put("Account Balance",new PoiConfig("amountBalance",null,new BigDecimal(3334444)));
```

## [1.0.1] - 2024-01-26
- **Compatibility Fix:** Downgraded Java library version from 21 to 11 to address compatibility issues.

## [1.0.0] - 2024-01-26
- Initial release of the library (java 21).