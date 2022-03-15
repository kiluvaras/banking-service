package ee.priit.pall.tuum.repository;

import ee.priit.pall.tuum.entity.Currency;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CurrencyRepository {

    @Select("SELECT * FROM currency")
    List<Currency> findAll();

    @Select("SELECT * FROM currency WHERE id = #{id}")
    Currency findById(Long id);

    @Select("SELECT * FROM currency WHERE iso_code = #{isoCode}")
    Currency findByIsoCode(String isoCode);

    @Select("SELECT iso_code FROM currency")
    List<String> findAllCurrencyCodes();
}
