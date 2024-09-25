package org.example.tictactoe.repository;

import org.example.tictactoe.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    // Aquí puedes definir consultas personalizadas si es necesario
}

