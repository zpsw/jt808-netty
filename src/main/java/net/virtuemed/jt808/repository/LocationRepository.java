package net.virtuemed.jt808.repository;

import net.virtuemed.jt808.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */
public interface LocationRepository extends JpaRepository<LocationEntity,Long> {
}
