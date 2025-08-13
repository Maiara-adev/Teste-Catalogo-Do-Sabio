package com.br.catalogodosabio.application.service;

import com.br.catalogodosabio.application.dto.BookDTO;
import com.br.catalogodosabio.application.service.RecentBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecentBooksServiceImpl implements RecentBooksService {

    private static final String KEY = "recent_books";
    private static final int MAX_SIZE = 10;

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    public RecentBooksServiceImpl(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<BookDTO> getRecentBooks() {
        List<Serializable> recentBooksObjects = redisTemplate.opsForList().range(KEY, 0, MAX_SIZE - 'l');
        if (recentBooksObjects == null) {
            return List.of();
        }
        return recentBooksObjects.stream()
                .map(obj -> (BookDTO) obj)
                .collect(Collectors.toList());
    }

    @Override
    public void addBookToRecent(BookDTO bookDTO) {
        redisTemplate.opsForList().remove(KEY, 0, bookDTO);
        redisTemplate.opsForList().leftPush(KEY, bookDTO);
        redisTemplate.opsForList().trim(KEY, 0, MAX_SIZE - 1);
        redisTemplate.expire(KEY, 24, TimeUnit.HOURS);
    }
}