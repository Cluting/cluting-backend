package com.cluting.clutingbackend.chat.controller;

import com.cluting.clutingbackend.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class ChatRoomController {

    private final ChatRoomRepository repository;

    //채팅방 목록 조회
//    @GetMapping(value = "/rooms")
//    public ModelAndView rooms(){
//
//        log.info("# All Chat Rooms");
//        ModelAndView mv = new ModelAndView("chat/rooms");
//
//        mv.addObject("list", repository.findAllRooms());
//
//        return mv;
//    }
//
//    //채팅방 개설
//    @PostMapping(value = "/room")
//    public String create(@RequestParam String name, RedirectAttributes rttr){
//
//        log.info("# Create Chat Room , name: " + name);
//        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));
//        return "redirect:/chat/rooms";
//    }

    @GetMapping("/rooms")
    public String getRooms(){
        return "chat/rooms";
    }
    @GetMapping(value = "/room")
    public String getRoom(Long chatRoomId, String nickname, Model model){

        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("nickname", nickname);

        return "chat/room";
    }
}
