package com.albaniaguessr.service.store;

import com.albaniaguessr.entity.GameSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryGameStore {

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    public GameSession save(GameSession session) {
        if (session.getId() == null) {
            session.setId(UUID.randomUUID());
        }
        sessions.put(session.getId(), session);
        log.debug("Game session saved: {}", session.getId());
        return session;
    }

    public GameSession findById(UUID id) {
        return sessions.get(id);
    }

    public GameSession findBySessionToken(String sessionToken) {
        return sessions.values().stream()
                .filter(s -> s.getSessionToken().equals(sessionToken))
                .findFirst()
                .orElse(null);
    }

    public List<GameSession> findAll() {
        return List.copyOf(sessions.values());
    }

    public List<GameSession> findByUserId(Long userId) {
        return sessions.values().stream()
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        sessions.remove(id);
    }

    public long count() {
        return sessions.size();
    }
}