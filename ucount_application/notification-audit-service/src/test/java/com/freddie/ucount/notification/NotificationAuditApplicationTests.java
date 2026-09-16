package com.freddie.ucount.notification;

import com.freddie.ucount.notification.entity.CommonComment;
import com.freddie.ucount.notification.service.CommonCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationAuditApplicationTests {

    @Autowired
    private CommonCommentService commentService;

    @Test
    void testAddAndRetrieveCommonComment() {
        CommonComment comment = new CommonComment("ENT-303", "ONBOARDING_SERVICE", "PA_PO", "Unit test comment payload.");
        CommonComment saved = commentService.addComment(comment);
        assertNotNull(saved.getId());

        List<CommonComment> list = commentService.getCommentsByEntityId("ENT-303");
        assertFalse(list.isEmpty());
        assertEquals("Unit test comment payload.", list.get(0).getCommentText());
    }
}
