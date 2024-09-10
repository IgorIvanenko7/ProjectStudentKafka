package kafkatests.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import kafkatests.dto.UserDto;
import kafkatests.service.HandlerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class RegController {

    private final HandlerService handlerService;
    private final NewRegistersReceiver newRegistersReceiver;

    // Регистрация посетителя конф.
    @PutMapping("/register")
    public ResponseEntity<Void> registerUser(
            @RequestBody UserDto requestDto) throws JsonProcessingException {
        handlerService.regUsers(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Получить всех новых участников конференции
    @GetMapping("/getNewRegisters/{conferenceId}")
    public List<String> getAllUsersConferenceMap (
            @PathVariable String conferenceId) {
        return handlerService.getListUsersV2(conferenceId);
    }

//    @GetMapping("/getNewRegisters/{conferenceId}")
//    public List<String> getAllUsersConference (
//            @PathVariable int conferenceId) {
//        return newRegistersReceiver.getNewRegisters(conferenceId);
//    }
}
