package com._oormthonUNIV.newnew.global.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LocalCacheService<K, V> {

    private final ConcurrentHashMap<K, CacheValue<V>> cache = new ConcurrentHashMap<>();
    private final DelayQueue<DelayedCacheItem<K>> delayQueue = new DelayQueue<>();

    private Thread cleanerThread;
    private volatile boolean running = true;

    /**
     * 캐시 put (TTL 포함)
     */
    public void put(K key, V value, long ttlMillis) {
        long version = System.nanoTime(); // 고유 버전 생성
        cache.put(key, new CacheValue<>(value, version));
        delayQueue.put(new DelayedCacheItem<>(key, ttlMillis, version));
    }

    /**
     * 단순 get
     */
    public V get(K key) {
        CacheValue<V> cv = cache.get(key);
        return cv != null ? cv.value : null;
    }

    /**
     * 필요하면 직접 삭제 가능
     */
    public void remove(K key) {
        cache.remove(key); // 제거
        // DelayQueue 안의 아이템은 CleanerThread에서 version mismatch로 무시됨
    }

    @PostConstruct
    public void startCleaner() {
        cleanerThread = new Thread(() -> {
            while (running) {
                try {
                    DelayedCacheItem<K> expiredItem = delayQueue.take();
                    CacheValue<V> cv = cache.get(expiredItem.key);
                    if (cv != null && cv.version == expiredItem.version) {
                        cache.remove(expiredItem.key);
                        log.info("[LocalCache] Key expired and removed: {}", expiredItem.key);
                    } else {
                        // 이미 갱신됐거나 제거됨 → 무시
                        log.debug("[LocalCache] Skip removing key: {}", expiredItem.key);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        cleanerThread.setDaemon(true);
        cleanerThread.start();
        log.info("[LocalCache] Cleaner thread started");
    }

    @PreDestroy
    public void shutdownCleaner() {
        running = false;
        cleanerThread.interrupt();
        log.info("[LocalCache] Cleaner thread stopped");
    }

    /**
     * DelayQueue 안에서 사용할 객체
     */
    private static class DelayedCacheItem<K> implements Delayed {

        private final K key;
        private final long expireTime;
        private final long version;

        public DelayedCacheItem(K key, long ttlMillis, long version) {
            this.key = key;
            this.expireTime = System.currentTimeMillis() + ttlMillis;
            this.version = version;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = expireTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),
                    o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 캐시 value + version
     */
    private static class CacheValue<V> {
        final V value;
        final long version;

        CacheValue(V value, long version) {
            this.value = value;
            this.version = version;
        }
    }
}
