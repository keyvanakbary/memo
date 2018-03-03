package com.keyvanakbary.memo.web;

import com.keyvanakbary.memo.api.post.CreatePostCommand;
import com.keyvanakbary.memo.api.post.FetchPostQuery;
import com.keyvanakbary.memo.api.post.UpdatePostCommand;
import com.keyvanakbary.memo.query.post.PostEntry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
@AllArgsConstructor
public class PostController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @GetMapping("/")
    public String showCreate() {
        return "show";
    }

    @PostMapping("/posts/create")
    public String create(@RequestParam("title") String title,
                         @RequestParam("slug") String slug,
                         @RequestParam("content") String content) {

        String id = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreatePostCommand(id, title, slug, content));
        return "redirect:/" + id;
    }

    @GetMapping("/{id}")
    public String showEdit(@PathVariable("id") String id, Model model) throws ExecutionException, InterruptedException {
        PostEntry p = queryGateway
            .send(new FetchPostQuery(id), PostEntry.class)
            .get();
        if (p == null) {
            throw new NotFound();
        }
        model.addAttribute("post", p);
        return "show";
    }

    @PostMapping("/posts/edit")
    public String edit(@RequestParam("id") String id,
                       @RequestParam("title") String title,
                       @RequestParam("slug") String slug,
                       @RequestParam("content") String content) {

        commandGateway.sendAndWait(new UpdatePostCommand(id, title, slug, content));

        return "redirect:/" + id;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private class NotFound extends RuntimeException {
    }
}
