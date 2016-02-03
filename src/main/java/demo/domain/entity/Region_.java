package demo.domain.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;

/**
 * Region metamodel.
 */
//CHECKSTYLE:OFF
@StaticMetamodel(Region.class)
public class Region_ {

    public static volatile SingularAttribute<Region, Integer> regionId;
    public static volatile SingularAttribute<Region, String> regionCode;
    public static volatile SingularAttribute<Region, String> regionDescription;
    public static volatile SingularAttribute<Region, String> regionMake;
    public static volatile SingularAttribute<Region, String> state;
    public static volatile SingularAttribute<Region, BigDecimal> latitude;
    public static volatile SingularAttribute<Region, BigDecimal> longitude;
    public static volatile SingularAttribute<Region, Franchise> franchise;
    public static volatile SingularAttribute<Region, Integer> radius;
}
//CHECKSTYLE:ON
