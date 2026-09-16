package com.freddie.ucount.notification.controller;

import com.freddie.ucount.notification.entity.AuditLogMessage;
import com.freddie.ucount.notification.entity.CommonComment;
import com.freddie.ucount.notification.service.CommonCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "*")
public class CommonCommentController {

    private final CommonCommentService commentService;

    public CommonCommentController(CommonCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CommonComment> addComment(@RequestBody CommonComment comment) {
        CommonComment saved = commentService.addComment(comment);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/entities/{entityId}")
    @ResponseBody
    public ResponseEntity<List<CommonComment>> getCommentsByEntityId(@PathVariable("entityId") String entityId) {
        return ResponseEntity.ok(commentService.getCommentsByEntityId(entityId));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CommonComment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/audits")
    @ResponseBody
    public ResponseEntity<List<AuditLogMessage>> getAllAuditLogs() {
        return ResponseEntity.ok(commentService.getAllAuditLogs());
    }

    // Hybrid UI Approach: JSP Page for Legacy Admin Review
    @GetMapping("/admin-review")
    public String renderAdminReviewPage(Model model) {
        model.addAttribute("comments", commentService.getAllComments());
        model.addAttribute("auditLogs", commentService.getAllAuditLogs());
        return "admin-review";
    }
}
