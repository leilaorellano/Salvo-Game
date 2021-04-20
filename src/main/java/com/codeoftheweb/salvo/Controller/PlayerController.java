package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Métodos//

    @RequestMapping("/players")
    public List<Object> getPlayersId() {
        return playerRepository.findAll().stream().map(player -> player.playerDTO()).collect(Collectors.toList());
    }
    // player singup
    // me permite verificar si mi registro de usuario cumple con algunas condiciones. Si cumple guarda al player nuevo y lo crea
    @PostMapping("/players") //REGISTER
    public ResponseEntity<Map<String, Object>> register(@RequestParam String email, @RequestParam String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>(makeMap("error", "name is already in use"), HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(makeMap("messege", "succes, player created"), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
