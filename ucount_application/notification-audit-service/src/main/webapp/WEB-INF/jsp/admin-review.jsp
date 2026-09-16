<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>UCount - Legacy Admin Review Portal</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #0f172a; color: #f8fafc; margin: 0; padding: 20px; }
        .container { max-width: 1200px; margin: 0 auto; }
        .header { border-bottom: 2px solid #3b82f6; padding-bottom: 15px; margin-bottom: 30px; }
        h1 { color: #60a5fa; margin: 0; }
        p.subtitle { color: #94a3b8; }
        .card { background: #1e293b; border-radius: 8px; padding: 20px; margin-bottom: 25px; border: 1px solid #334155; }
        h2 { color: #38bdf8; margin-top: 0; border-bottom: 1px solid #334155; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #334155; }
        th { background: #0f172a; color: #94a3b8; text-transform: uppercase; font-size: 0.85rem; letter-spacing: 0.05em; }
        tr:hover { background: #334155; }
        .tag { display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: bold; background: #2563eb; color: #ffffff; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>UCount Counterparty Management System</h1>
        <p class="subtitle">Legacy Administration & Audit Review Dashboard (JSP Integration)</p>
    </div>

    <div class="card">
        <h2>Global Common Comments Log</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Entity ID</th>
                    <th>Service Origin</th>
                    <th>Author Role</th>
                    <th>Comment Detail</th>
                    <th>Timestamp</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="comment" items="${comments}">
                    <tr>
                        <td>${comment.id}</td>
                        <td><strong>${comment.entityId}</strong></td>
                        <td><span class="tag">${comment.serviceOrigin}</span></td>
                        <td>${comment.authorRole}</td>
                        <td>${comment.commentText}</td>
                        <td>${comment.createdAt}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty comments}">
                    <tr><td colspan="6" style="text-align:center; color:#94a3b8;">No comments logged yet. Use Angular UI or REST API to add comments.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="card">
        <h2>ActiveMQ JMS Audit Log Stream</h2>
        <table>
            <thead>
                <tr>
                    <th>Log ID</th>
                    <th>Correlation ID</th>
                    <th>Event Source</th>
                    <th>Payload Detail</th>
                    <th>Received At</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="audit" items="${auditLogs}">
                    <tr>
                        <td>${audit.id}</td>
                        <td><code>${audit.correlationId}</code></td>
                        <td>${audit.eventSource}</td>
                        <td>${audit.messagePayload}</td>
                        <td>${audit.receivedAt}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty auditLogs}">
                    <tr><td colspan="5" style="text-align:center; color:#94a3b8;">No audit logs captured.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
