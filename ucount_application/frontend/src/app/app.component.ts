import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="app-layout">
      <!-- Top Navbar -->
      <header class="navbar">
        <div class="logo">
          <div class="logo-badge">FM</div>
          <span>UCount Counterparty Platform</span>
        </div>
        <div class="nav-links">
          <button [class.active]="activeTab === 'intake'" (click)="activeTab = 'intake'">Stage 1 Intake</button>
          <button [class.active]="activeTab === 'stage2'" (click)="activeTab = 'stage2'">Stage 2 Profiles</button>
          <button [class.active]="activeTab === 'rules'" (click)="activeTab = 'rules'">Rules Engine</button>
          <button [class.active]="activeTab === 'acr'" (click)="activeTab = 'acr'">ControlM ACR Batch</button>
          <button [class.active]="activeTab === 'comments'" (click)="activeTab = 'comments'">Comments & Audits</button>
          <a href="http://localhost:8083/api/v1/comments/admin-review" target="_blank" class="jsp-link">JSP Admin View ↗</a>
        </div>
      </header>

      <!-- Main Container -->
      <main class="main-content">

        <!-- TAB 1: STAGE 1 INTAKE FORM & APPROVAL -->
        <section *ngIf="activeTab === 'intake'" class="tab-section">
          <div class="grid-layout">
            <div class="card">
              <h2>Stage 1: User Onboarding Intake (DB1)</h2>
              <p class="card-subtitle">Captures initial organization and network credentials for investigation.</p>

              <form (ngSubmit)="submitStage1Intake()" class="form-grid">
                <div class="form-group">
                  <label>User ID / System Code</label>
                  <input type="text" [(ngModel)]="stage1Form.userId" name="userId" placeholder="e.g. USR-9021" required>
                </div>
                <div class="form-group">
                  <label>Organization Name</label>
                  <input type="text" [(ngModel)]="stage1Form.organizationName" name="organizationName" placeholder="e.g. Crestview Mortgage Corp" required>
                </div>
                <div class="form-group">
                  <label>Tax Identification Number</label>
                  <input type="text" [(ngModel)]="stage1Form.taxId" name="taxId" placeholder="e.g. TX-8839201" required>
                </div>
                <div class="form-group">
                  <label>Work Email</label>
                  <input type="email" [(ngModel)]="stage1Form.workEmail" name="workEmail" placeholder="officer@crestview.com" required>
                </div>
                <div class="form-group">
                  <label>Contact Number</label>
                  <input type="text" [(ngModel)]="stage1Form.contactNumber" name="contactNumber" placeholder="+1 (555) 019-2834" required>
                </div>
                <div class="form-group">
                  <label>Work Address</label>
                  <input type="text" [(ngModel)]="stage1Form.workAddress" name="workAddress" placeholder="700 Virginia Ave, McLean, VA" required>
                </div>
                <div class="form-group">
                  <label>Network Domain</label>
                  <input type="text" [(ngModel)]="stage1Form.networkDomain" name="networkDomain" placeholder="crestview.com" required>
                </div>
                <div class="form-group">
                  <label>Counterparty User Type</label>
                  <select [(ngModel)]="stage1Form.userType" name="userType">
                    <option value="HOUSE_SELLER">House Seller</option>
                    <option value="HOUSE_BUYER">House Buyer</option>
                    <option value="INSURANCE_AGENT">Insurance Agent</option>
                    <option value="MORTGAGE_SERVICER">Mortgage Servicer</option>
                  </select>
                </div>

                <div class="form-actions">
                  <button type="submit" class="btn-primary">Register Stage 1 User</button>
                </div>
              </form>
            </div>

            <!-- Stage 1 Users Table -->
            <div class="card">
              <h2>Registered Stage 1 Users</h2>
              <table class="data-table">
                <thead>
                  <tr>
                    <th>User ID</th>
                    <th>Organization</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let u of stage1Users">
                    <td><strong>{{ u.userId }}</strong></td>
                    <td>{{ u.organizationName }}</td>
                    <td><span class="badge" [class.badge-pending]="u.approvalStatus === 'PENDING_APPROVAL'" [class.badge-approved]="u.approvalStatus === 'APPROVED'">{{ u.approvalStatus }}</span></td>
                    <td>
                      <button *ngIf="u.approvalStatus === 'PENDING_APPROVAL'" class="btn-sm btn-success" (click)="approveUser(u.userId)">Approve & Promote</button>
                      <span *ngIf="u.approvalStatus === 'APPROVED'" class="text-muted">Promoted to Stage 2</span>
                    </td>
                  </tr>
                  <tr *ngIf="stage1Users.length === 0">
                    <td colspan="4" class="text-center">No Stage 1 users registered yet.</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <!-- TAB 2: STAGE 2 PROFILES & ENTITLEMENTS -->
        <section *ngIf="activeTab === 'stage2'" class="tab-section">
          <div class="card">
            <h2>Stage 2: Counterparty Profiles & Granted Entitlements (DB2)</h2>
            <p class="card-subtitle">Active Freddie Mac counterparty records with provisioned default service accesses.</p>

            <table class="data-table">
              <thead>
                <tr>
                  <th>Profile ID</th>
                  <th>User ID</th>
                  <th>User Type</th>
                  <th>Financial Rating</th>
                  <th>Compliance Status</th>
                  <th>Profile Status</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let p of stage2Profiles">
                  <td><code>{{ p.profileId }}</code></td>
                  <td><strong>{{ p.userId }}</strong></td>
                  <td><span class="tag-type">{{ p.userType }}</span></td>
                  <td>{{ p.financialHistoryRating }}</td>
                  <td><span class="badge badge-approved">PASSED</span></td>
                  <td><span class="badge badge-active">{{ p.status }}</span></td>
                </tr>
                <tr *ngIf="stage2Profiles.length === 0">
                  <td colspan="6" class="text-center">No Stage 2 profiles found. Approve Stage 1 users to generate Stage 2 records.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <!-- TAB 3: RULES ENGINE EVALUATOR -->
        <section *ngIf="activeTab === 'rules'" class="tab-section">
          <div class="card">
            <h2>User Type Entitlement Rules Engine</h2>
            <p class="card-subtitle">Test rules for assigning default Freddie Mac application rights based on User Type.</p>

            <div class="rules-eval-bar">
              <label>Select User Type to Evaluate:</label>
              <select [(ngModel)]="selectedRuleType" (change)="evaluateRules()">
                <option value="HOUSE_SELLER">House Seller</option>
                <option value="HOUSE_BUYER">House Buyer</option>
                <option value="INSURANCE_AGENT">Insurance Agent</option>
                <option value="MORTGAGE_SERVICER">Mortgage Servicer</option>
              </select>
            </div>

            <div class="rules-grid">
              <div class="rule-card" *ngFor="let r of evaluatedRules">
                <div class="rule-title">{{ r.serviceName }}</div>
                <div class="rule-perm" [class.perm-full]="r.permissionLevel === 'FULL_ACCESS'" [class.perm-read]="r.permissionLevel === 'READ_ONLY'">{{ r.permissionLevel }}</div>
              </div>
            </div>
          </div>
        </section>

        <!-- TAB 4: CONTROLM ACR BATCH -->
        <section *ngIf="activeTab === 'acr'" class="tab-section">
          <div class="grid-layout">
            <div class="card">
              <h2>ControlM ACR (Annual Certificate Repair) Batch Job</h2>
              <p class="card-subtitle">Automated background job to archive old/expired counterparty records to transaction history and purge main tables.</p>

              <button class="btn-primary btn-lg" (click)="triggerAcrBatch()">⚡ Run ControlM ACR Batch Job Now</button>

              <div *ngIf="acrResult" class="acr-result-box">
                <h4>Batch Execution Result:</h4>
                <p>Status: <strong>{{ acrResult.success ? 'SUCCESS' : 'FAILED' }}</strong></p>
                <p>Records Archived & Purged: <strong>{{ acrResult.recordsArchived }}</strong></p>
                <p>Message: {{ acrResult.message }}</p>
              </div>
            </div>

            <div class="card">
              <h2>Archived Transactions Log</h2>
              <table class="data-table">
                <thead>
                  <tr>
                    <th>Original Profile</th>
                    <th>User ID</th>
                    <th>Reason</th>
                    <th>Archived At</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let a of archivedLogs">
                    <td><code>{{ a.originalProfileId }}</code></td>
                    <td>{{ a.userId }}</td>
                    <td><span class="tag-archive">{{ a.archiveReason }}</span></td>
                    <td>{{ a.archivedAt }}</td>
                  </tr>
                  <tr *ngIf="archivedLogs.length === 0">
                    <td colspan="4" class="text-center">No transactions archived yet. Trigger the ACR job to purge old records.</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <!-- TAB 5: COMMENTS & AUDIT LOGS -->
        <section *ngIf="activeTab === 'comments'" class="tab-section">
          <div class="grid-layout">
            <div class="card">
              <h2>Common Comment API</h2>
              <p class="card-subtitle">Add a common comment shared across all microservices.</p>

              <form (ngSubmit)="addComment()" class="form-grid">
                <div class="form-group">
                  <label>Entity ID (User or Profile)</label>
                  <input type="text" [(ngModel)]="commentForm.entityId" name="entityId" placeholder="USR-9021" required>
                </div>
                <div class="form-group">
                  <label>Service Origin</label>
                  <select [(ngModel)]="commentForm.serviceOrigin" name="serviceOrigin">
                    <option value="ONBOARDING_SERVICE">Onboarding Service</option>
                    <option value="ACCESS_DECISION_SERVICE">Access Decision Service</option>
                    <option value="COMPLIANCE_AUDIT">Compliance Audit</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>Author Role</label>
                  <select [(ngModel)]="commentForm.authorRole" name="authorRole">
                    <option value="PA_PO">PA / PO Lead</option>
                    <option value="LEAD_DEV">Lead Developer</option>
                    <option value="AUDITOR">Security Auditor</option>
                  </select>
                </div>
                <div class="form-group full-width">
                  <label>Comment Text</label>
                  <textarea [(ngModel)]="commentForm.commentText" name="commentText" rows="3" placeholder="Enter review or investigation notes..." required></textarea>
                </div>
                <div class="form-actions">
                  <button type="submit" class="btn-primary">Post Global Comment</button>
                </div>
              </form>
            </div>

            <div class="card">
              <h2>Global Comments & Audit Stream</h2>
              <div class="comments-list">
                <div class="comment-item" *ngFor="let c of commentsList">
                  <div class="comment-header">
                    <strong>{{ c.entityId }}</strong>
                    <span class="badge badge-approved">{{ c.authorRole }}</span>
                    <span class="text-muted">{{ c.serviceOrigin }}</span>
                  </div>
                  <div class="comment-body">{{ c.commentText }}</div>
                </div>
                <div *ngIf="commentsList.length === 0" class="text-center text-muted">No comments logged.</div>
              </div>
            </div>
          </div>
        </section>

      </main>
    </div>
  `,
  styles: [`
    .app-layout { display: flex; flex-direction: column; min-height: 100vh; background: #0b0f19; color: #f1f5f9; }
    .navbar { display: flex; justify-content: space-between; align-items: center; padding: 15px 30px; background: #111827; border-bottom: 1px solid #1f2937; }
    .logo { display: flex; align-items: center; gap: 12px; font-weight: 700; font-size: 1.2rem; color: #38bdf8; }
    .logo-badge { background: linear-gradient(135deg, #2563eb, #0284c7); padding: 6px 12px; border-radius: 6px; color: #fff; font-size: 0.9rem; }
    .nav-links { display: flex; gap: 10px; align-items: center; }
    .nav-links button { background: transparent; border: 1px solid #374151; color: #9ca3af; padding: 8px 16px; border-radius: 6px; cursor: pointer; transition: all 0.2s; }
    .nav-links button:hover, .nav-links button.active { background: #2563eb; color: #fff; border-color: #2563eb; }
    .jsp-link { color: #f59e0b; text-decoration: none; padding: 8px 14px; border: 1px solid #d97706; border-radius: 6px; font-size: 0.9rem; }
    .main-content { padding: 30px; max-width: 1400px; margin: 0 auto; width: 100%; box-sizing: border-box; }
    .grid-layout { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
    .card { background: #1f2937; border-radius: 10px; padding: 24px; border: 1px solid #374151; box-shadow: 0 4px 12px rgba(0,0,0,0.3); }
    .card h2 { margin-top: 0; color: #38bdf8; font-size: 1.3rem; margin-bottom: 6px; }
    .card-subtitle { color: #9ca3af; margin-bottom: 20px; font-size: 0.9rem; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-group { display: flex; flex-direction: column; gap: 6px; }
    .form-group.full-width { grid-column: span 2; }
    .form-group label { font-size: 0.85rem; color: #9ca3af; }
    .form-group input, .form-group select, .form-group textarea { background: #111827; border: 1px solid #374151; border-radius: 6px; padding: 10px; color: #f1f5f9; }
    .form-actions { grid-column: span 2; margin-top: 10px; }
    .btn-primary { background: linear-gradient(135deg, #2563eb, #1d4ed8); border: none; color: white; padding: 12px 24px; border-radius: 6px; cursor: pointer; font-weight: 600; width: 100%; }
    .btn-sm { padding: 6px 12px; font-size: 0.8rem; border-radius: 4px; border: none; cursor: pointer; }
    .btn-success { background: #10b981; color: white; }
    .data-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
    .data-table th, .data-table td { padding: 12px; text-align: left; border-bottom: 1px solid #374151; font-size: 0.9rem; }
    .data-table th { background: #111827; color: #9ca3af; text-transform: uppercase; font-size: 0.75rem; }
    .badge { padding: 4px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 600; }
    .badge-pending { background: #d97706; color: white; }
    .badge-approved { background: #059669; color: white; }
    .badge-active { background: #2563eb; color: white; }
    .tag-type { background: #4f46e5; padding: 3px 8px; border-radius: 4px; font-size: 0.8rem; }
    .tag-archive { background: #7c3aed; padding: 3px 8px; border-radius: 4px; font-size: 0.8rem; }
    .text-center { text-align: center; }
    .text-muted { color: #6b7280; }
    .rules-eval-bar { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
    .rules-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 16px; }
    .rule-card { background: #111827; padding: 16px; border-radius: 8px; border: 1px solid #374151; }
    .rule-title { font-weight: 600; margin-bottom: 8px; color: #f3f4f6; }
    .rule-perm { display: inline-block; padding: 4px 10px; border-radius: 4px; font-size: 0.8rem; font-weight: bold; }
    .perm-full { background: #059669; color: white; }
    .perm-read { background: #0284c7; color: white; }
    .acr-result-box { background: #111827; border: 1px solid #059669; border-radius: 8px; padding: 16px; margin-top: 20px; }
    .comments-list { display: flex; flex-direction: column; gap: 12px; }
    .comment-item { background: #111827; border: 1px solid #374151; padding: 12px; border-radius: 6px; }
    .comment-header { display: flex; gap: 10px; align-items: center; margin-bottom: 6px; font-size: 0.85rem; }
  `]
})
export class AppComponent implements OnInit {
  activeTab = 'intake';

  stage1Form = {
    userId: 'USR-1001',
    organizationName: 'Apex Mortgage Partners',
    taxId: 'TX-998822',
    workEmail: 'intake@apexmortgage.com',
    contactNumber: '+1 (555) 234-5678',
    workAddress: '100 Tyson Blvd, McLean, VA',
    networkDomain: 'apexmortgage.com',
    userType: 'HOUSE_SELLER'
  };

  stage1Users: any[] = [];
  stage2Profiles: any[] = [];
  evaluatedRules: any[] = [];
  selectedRuleType = 'HOUSE_SELLER';
  acrResult: any = null;
  archivedLogs: any[] = [];

  commentForm = {
    entityId: 'USR-1001',
    serviceOrigin: 'ONBOARDING_SERVICE',
    authorRole: 'PA_PO',
    commentText: 'Initial Stage 1 details validated against state licensing registry.'
  };
  commentsList: any[] = [];

  ngOnInit() {
    this.fetchStage1Users();
    this.fetchStage2Profiles();
    this.evaluateRules();
    this.fetchComments();
  }

  submitStage1Intake() {
    const newUser = {
      userId: this.stage1Form.userId,
      organizationName: this.stage1Form.organizationName,
      taxId: this.stage1Form.taxId,
      workEmail: this.stage1Form.workEmail,
      contactNumber: this.stage1Form.contactNumber,
      workAddress: this.stage1Form.workAddress,
      networkDomain: this.stage1Form.networkDomain,
      approvalStatus: 'PENDING_APPROVAL'
    };
    this.stage1Users.push(newUser);
    alert('Stage 1 User intake registered successfully!');
  }

  approveUser(userId: string) {
    const u = this.stage1Users.find(x => x.userId === userId);
    if (u) {
      u.approvalStatus = 'APPROVED';
      const newProfile = {
        profileId: 'PRFL-' + Math.floor(1000 + Math.random() * 9000),
        userId: userId,
        userType: this.stage1Form.userType,
        financialHistoryRating: 'TIER_1_PRIME',
        status: 'ACTIVE'
      };
      this.stage2Profiles.push(newProfile);
      alert('User ' + userId + ' approved in Stage 1 and promoted to Stage 2 Profile!');
    }
  }

  evaluateRules() {
    if (this.selectedRuleType === 'HOUSE_SELLER') {
      this.evaluatedRules = [
        { serviceName: 'LOAN_ORIGINATION_PORTAL', permissionLevel: 'FULL_ACCESS' },
        { serviceName: 'PROPERTY_APPRAISAL_GATEWAY', permissionLevel: 'FULL_ACCESS' },
        { serviceName: 'TITLE_INSURANCE_PORTAL', permissionLevel: 'READ_ONLY' }
      ];
    } else if (this.selectedRuleType === 'HOUSE_BUYER') {
      this.evaluatedRules = [
        { serviceName: 'LOAN_APPLICATION_PORTAL', permissionLevel: 'FULL_ACCESS' },
        { serviceName: 'CREDIT_BUREAU_PORTAL', permissionLevel: 'READ_ONLY' },
        { serviceName: 'DOCUMENT_VAULT', permissionLevel: 'FULL_ACCESS' }
      ];
    } else if (this.selectedRuleType === 'INSURANCE_AGENT') {
      this.evaluatedRules = [
        { serviceName: 'TITLE_INSURANCE_PORTAL', permissionLevel: 'FULL_ACCESS' },
        { serviceName: 'ESCROW_MANAGEMENT_PORTAL', permissionLevel: 'FULL_ACCESS' }
      ];
    } else {
      this.evaluatedRules = [
        { serviceName: 'LOAN_SERVICING_SYSTEM', permissionLevel: 'FULL_ACCESS' },
        { serviceName: 'SECONDARY_MARKET_ACCESS', permissionLevel: 'FULL_ACCESS' }
      ];
    }
  }

  triggerAcrBatch() {
    this.acrResult = {
      success: true,
      recordsArchived: this.stage2Profiles.length > 0 ? 1 : 0,
      message: 'ControlM ACR Annual Certificate Repair job completed successfully.'
    };
    if (this.stage2Profiles.length > 0) {
      const removed = this.stage2Profiles.pop();
      this.archivedLogs.push({
        originalProfileId: removed.profileId,
        userId: removed.userId,
        archiveReason: 'CONTROLM_ACR_ANNUAL_CERTIFICATE_REPAIR_ARCHIVE',
        archivedAt: new Date().toISOString()
      });
    }
  }

  addComment() {
    this.commentsList.unshift({
      entityId: this.commentForm.entityId,
      serviceOrigin: this.commentForm.serviceOrigin,
      authorRole: this.commentForm.authorRole,
      commentText: this.commentForm.commentText
    });
    alert('Global comment posted successfully!');
  }

  fetchStage1Users() {
    this.stage1Users = [
      { userId: 'USR-9001', organizationName: 'Freedom Financial LLC', approvalStatus: 'APPROVED' },
      { userId: 'USR-9002', organizationName: 'Beacon Capital Mortgage', approvalStatus: 'PENDING_APPROVAL' }
    ];
  }

  fetchStage2Profiles() {
    this.stage2Profiles = [
      { profileId: 'PRFL-8812', userId: 'USR-9001', userType: 'HOUSE_SELLER', financialHistoryRating: 'TIER_1_PRIME', status: 'ACTIVE' }
    ];
  }

  fetchComments() {
    this.commentsList = [
      { entityId: 'USR-9001', serviceOrigin: 'ONBOARDING_SERVICE', authorRole: 'PA_PO', commentText: 'Organization credentials and tax filing verified.' }
    ];
  }
}
