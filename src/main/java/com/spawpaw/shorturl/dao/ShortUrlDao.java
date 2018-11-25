package com.spawpaw.shorturl.dao;

import com.spawpaw.shorturl.entity.ShortUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlDao extends CrudRepository<ShortUrl, Long> {

    ShortUrl findByRawUrl(String rawUrl);
//    @Query("SELECT id FROM shortUrl WHERE  LIMIT 1,1 ORDER BY id DESC ")
//    Long getMaxId();
}
