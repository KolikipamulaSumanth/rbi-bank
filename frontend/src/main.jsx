import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import {
  BadgeIndianRupee,
  Banknote,
  Bell,
  BookOpen,
  BriefcaseBusiness,
  Building2,
  CreditCard,
  Download,
  FileCheck2,
  FileText,
  HandCoins,
  Landmark,
  Lock,
  LogOut,
  Search,
  Send,
  ShieldAlert,
  ShieldCheck,
  Ticket,
  UserCog,
  UserPlus,
  Wallet,
} from "lucide-react";
import "./styles.css";

const API_BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

const demoProfile = {
  bankName: "RBI Bank",
  customerId: "RBI7429012",
  accountNumber: "7348291056",
  ifscCode: "RBI0INR4821",
  accountType: "Savings",
  accountStatus: "Active",
  balance: 25000,
  panNumber: "ABCDE1234F",
  aadhaarNumber: "XXXX XXXX 9012",
  address: "12 Banking Street, Mumbai, Maharashtra",
  contact: "customer@rbibank.local | 9876543210",
  atmCardNumber: "4213768450129012",
};

const demoTransactions = [
  { txId: "TXN10041", type: "CREDIT", amount: 12000, status: "COMPLETED", createdAt: "2026-05-12T10:15:00", senderAccountNumber: null, receiverAccountNumber: "7348291056", counterpartyName: "RBI Bank Branch", description: "Branch cash deposit", balanceAfterTransaction: 25000 },
  { txId: "TXN10042", type: "DEBIT", amount: 3500, status: "COMPLETED", createdAt: "2026-05-13T14:22:00", senderAccountNumber: "7348291056", receiverAccountNumber: "9031846257", counterpartyName: "Nisha Iyer", description: "Internal rent payment", balanceAfterTransaction: 21500 },
  { txId: "TXN10043", type: "DEBIT", amount: 2000, status: "COMPLETED", createdAt: "2026-05-15T11:05:00", senderAccountNumber: "7348291056", receiverAccountNumber: null, counterpartyName: "RBI Bank Branch", description: "Branch cash withdrawal", balanceAfterTransaction: 19500 },
  { txId: "TXN10044", type: "TRANSFER", amount: 85000, status: "PENDING", createdAt: "2026-05-16T16:40:00", senderAccountNumber: "9031846257", receiverAccountNumber: "6510298437", counterpartyName: "Rahul Nair", description: "High value review", balanceAfterTransaction: 57000 },
];

const staffAccounts = [
  { name: "Aarav Sharma", accountNumber: "7348291056", pan: "ABCDE1234F", phone: "9876543210", balance: 25000, status: "ACTIVE", kyc: "VERIFIED" },
  { name: "Nisha Iyer", accountNumber: "9031846257", pan: "PQRSX2190A", phone: "9812345670", balance: 142000, status: "ACTIVE", kyc: "PENDING" },
  { name: "Rahul Nair", accountNumber: "6510298437", pan: "LMNOP9087C", phone: "9900011112", balance: 6400, status: "FROZEN", kyc: "VERIFIED" },
];

function App() {
  const [session, setSession] = useState(null);
  const [token, setToken] = useState("");
  const [login, setLogin] = useState({ username: "", password: "" });
  const [message, setMessage] = useState("Use customer, employee, or admin with Password123.");
  const [accounts, setAccounts] = useState([]);
  const [card, setCard] = useState(null);
  const [transactions, setTransactions] = useState(demoTransactions);
  const [activePage, setActivePage] = useState("overview");
  const [amount, setAmount] = useState("");
  const [recipient, setRecipient] = useState("");
  const [remarks, setRemarks] = useState("");
  const [staffSearch, setStaffSearch] = useState("");
  const [statementDays, setStatementDays] = useState("7");
  const [requestPage, setRequestPage] = useState("hub");
  const [customerRequests, setCustomerRequests] = useState([]);
  const [staffRequests, setStaffRequests] = useState([]);

  const role = session?.roles?.includes("ADMIN") ? "ADMIN" : session?.roles?.includes("EMPLOYEE") ? "EMPLOYEE" : "USER";
  const profile = useMemo(() => buildProfile(session, accounts, card), [session, accounts, card]);
  const authHeaders = token ? { Authorization: token } : {};

  useEffect(() => {
    if (session && token) {
      loadAccounts(token);
      loadCard(token);
      loadTransactions(token);
      loadServiceRequests(token, role);
    }
  }, [session, token]);

  async function request(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers: {
        ...(options.body ? { "Content-Type": "application/json" } : {}),
        ...authHeaders,
        ...(options.headers || {}),
      },
    });
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;
    if (!response.ok) {
      if (response.status === 401 || response.status === 403) {
        throw new Error(data?.message || "Session expired or password is incorrect. Please logout and login again.");
      }
      throw new Error(data?.message || `HTTP ${response.status}`);
    }
    return { response, data };
  }

  async function authenticate(event) {
    event.preventDefault();
    setMessage("Checking credentials...");
    try {
      const { response, data } = await request("/user/auth", { method: "POST", body: JSON.stringify(login) });
      const jwt = response.headers.get("Authorization") || "";
      setToken(jwt);
      setSession(data);
      setLogin({ username: "", password: "" });
      setMessage("Signed in.");
      setActivePage("overview");
      setAccounts([]);
    } catch (error) {
      setMessage(`Login failed: ${error.message}`);
      setLogin((current) => ({ ...current, password: "" }));
    }
  }

  function logout() {
    setSession(null);
    setToken("");
    setLogin({ username: "", password: "" });
    setAccounts([]);
    setCard(null);
    setTransactions(demoTransactions);
    setCustomerRequests([]);
    setStaffRequests([]);
    setAmount("");
    setRecipient("");
    setRemarks("");
    setRequestPage("hub");
    setStaffSearch("");
    setActivePage("overview");
    setMessage("Signed out. Please login again.");
  }

  async function loadAccounts(jwt = token) {
    try {
      const response = await fetch(`${API_BASE}/accounts`, { headers: { Authorization: jwt } });
      if (!response.ok) return;
      setAccounts(await response.json());
    } catch {
      setAccounts([]);
    }
  }

  async function loadCard(jwt = token) {
    try {
      const response = await fetch(`${API_BASE}/card`, { headers: { Authorization: jwt } });
      if (!response.ok) {
        setCard(null);
        return;
      }
      setCard(await response.json());
    } catch {
      setCard(null);
    }
  }

  async function loadTransactions(jwt = token) {
    try {
      const response = await fetch(`${API_BASE}/transactions?page=0`, { headers: { Authorization: jwt } });
      if (!response.ok) return;
      const rows = await response.json();
      setTransactions(rows.length ? rows : demoTransactions);
    } catch {
      setTransactions(demoTransactions);
    }
  }

  async function transferMoney() {
    const account = accounts[0];
    const transferAmount = Number(amount);
    if (!recipient || !transferAmount || transferAmount <= 0) {
      setMessage("Transfer failed: Enter a valid recipient account number and amount.");
      return;
    }
    if (transferAmount > Number(profile.balance || 0)) {
      setMessage("Transfer failed: Insufficient balance.");
      setTransactions((rows) => [
        { txId: `TXN${Date.now()}`, type: "TRANSFER", amount: transferAmount, status: "FAILED", createdAt: new Date().toISOString(), senderAccountNumber: profile.accountNumber, receiverAccountNumber: recipient, counterpartyName: "Not completed", description: "Insufficient balance", balanceAfterTransaction: profile.balance },
        ...rows,
      ]);
      return;
    }
    const payload = {
      code: account?.code || "INR",
      accountNumber: Number(account?.accountNumber || profile.accountNumber),
      recipientAccountNumber: Number(recipient),
      amount: transferAmount,
      remarks,
    };
    setMessage("Fund transfer submitted...");
    try {
      await request("/accounts/transfer", { method: "POST", body: JSON.stringify(payload) });
      await loadAccounts();
      await loadTransactions();
      setMessage("Internal fund transfer completed.");
      setAmount("");
      setRecipient("");
      setRemarks("");
    } catch (error) {
      setMessage(`Transfer failed: ${error.message}`);
      setTransactions((rows) => [
        { txId: `TXN${Date.now()}`, type: "TRANSFER", amount: transferAmount, status: "FAILED", createdAt: new Date().toISOString(), senderAccountNumber: profile.accountNumber, receiverAccountNumber: recipient, counterpartyName: "Not completed", description: error.message, balanceAfterTransaction: profile.balance },
        ...rows,
      ]);
    }
  }

  async function branchCashAction(kind, accountNumber, cashAmount) {
    const path = kind === "deposit" ? "/accounts/deposit" : "/accounts/withdraw";
    await request(path, {
      method: "POST",
      body: JSON.stringify({ accountNumber: Number(accountNumber), amount: Number(cashAmount), code: "INR" }),
    });
  }

  async function changePassword(currentPassword, newPassword) {
    const { data } = await request("/user/password", {
      method: "POST",
      body: JSON.stringify({ currentPassword, newPassword }),
    });
    setMessage(data?.message || "Password changed successfully");
    return data;
  }

  async function loadServiceRequests(jwt = token, currentRole = role) {
    const path = currentRole === "USER" ? "/requests/mine" : "/requests";
    try {
      const response = await fetch(`${API_BASE}${path}`, { headers: { Authorization: jwt } });
      if (!response.ok) return;
      const data = await response.json();
      if (currentRole === "USER") {
        setCustomerRequests(data);
      } else {
        setStaffRequests(data);
      }
    } catch {
      if (currentRole === "USER") setCustomerRequests([]);
      else setStaffRequests([]);
    }
  }

  async function submitServiceRequest(requestType, details) {
    const { data } = await request("/requests", {
      method: "POST",
      body: JSON.stringify({ requestType, requestData: JSON.stringify(details) }),
    });
    setCustomerRequests((rows) => [data, ...rows]);
    setMessage(`${data.requestId} submitted successfully.`);
    return data;
  }

  async function updateServiceRequest(id, status, approvalRemarks, assignedTo = "") {
    const { data } = await request(`/requests/${id}`, {
      method: "PATCH",
      body: JSON.stringify({ status, approvalRemarks, assignedTo }),
    });
    setStaffRequests((rows) => rows.map((row) => (row.id === id ? data : row)));
    setMessage(`${data.requestId} updated to ${titleCase(data.status)}.`);
    return data;
  }

  async function staffAction(label, callback) {
    setMessage(`${label}...`);
    try {
      await callback?.();
      setMessage(`${label} completed.`);
    } catch (error) {
      setMessage(`${label} failed: ${error.message}`);
    }
  }

  if (!session) {
    return (
      <main className="login-screen">
        <form className="login-panel" onSubmit={authenticate} autoComplete="off">
          <div className="bank-mark"><Landmark size={32} /></div>
          <h1>RBI Bank</h1>
          <label>
            <span>Username</span>
            <input value={login.username} onChange={(event) => setLogin({ ...login, username: event.target.value })} autoComplete="off" name="rbi-bank-user" />
          </label>
          <label>
            <span>Password</span>
            <input type="password" value={login.password} onChange={(event) => setLogin({ ...login, password: event.target.value })} autoComplete="new-password" name="rbi-bank-pass" />
          </label>
          <button className="primary-button" type="submit">
            <Lock size={17} />
            <span>Login</span>
          </button>
          <p className="login-message">{message}</p>
        </form>
      </main>
    );
  }

  const nav = role === "USER" ? customerNav : role === "ADMIN" ? adminNav : employeeNav;

  return (
    <main className="bank-shell">
      <aside className="sidebar">
        <div className="brand-row">
          <Landmark size={28} />
          <div>
            <strong>RBI Bank</strong>
            <span>{role === "USER" ? "Customer banking" : "Branch operations"}</span>
          </div>
        </div>
        <nav>
          {nav.map((item) => (
            <button key={item.id} className={activePage === item.id ? "active" : ""} onClick={() => setActivePage(item.id)}>
              <item.icon size={18} />
              <span>{item.label}</span>
            </button>
          ))}
        </nav>
        <button className="logout" onClick={logout}>
          <LogOut size={17} />
          <span>Logout</span>
        </button>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p>{role}</p>
            <h1>{session.firstname} {session.lastname}</h1>
          </div>
          <div className="status-line"><Bell size={16} />{message}</div>
        </header>

        {role === "USER" ? (
          <CustomerView page={activePage} profile={profile} accounts={accounts} amount={amount} recipient={recipient} remarks={remarks} setAmount={setAmount} setRecipient={setRecipient} setRemarks={setRemarks} transferMoney={transferMoney} transactions={transactions} statementDays={statementDays} setStatementDays={setStatementDays} changePassword={changePassword} requestPage={requestPage} setRequestPage={setRequestPage} requests={customerRequests} submitServiceRequest={submitServiceRequest} />
        ) : (
          <StaffView page={activePage} role={role} search={staffSearch} setSearch={setStaffSearch} staffAction={staffAction} branchCashAction={branchCashAction} requests={staffRequests} updateServiceRequest={updateServiceRequest} />
        )}
      </section>
    </main>
  );
}

const customerNav = [
  { id: "overview", label: "Dashboard", icon: Wallet },
  { id: "profile", label: "Profile", icon: FileCheck2 },
  { id: "transactions", label: "Transactions", icon: BookOpen },
  { id: "services", label: "Requests", icon: Ticket },
  { id: "security", label: "Security", icon: Lock },
];

const employeeNav = [
  { id: "overview", label: "Dashboard", icon: BriefcaseBusiness },
  { id: "customers", label: "Customers", icon: Search },
  { id: "operations", label: "Operations", icon: HandCoins },
  { id: "requests", label: "Employee Queue", icon: Ticket },
  { id: "risk", label: "Risk Review", icon: ShieldAlert },
  { id: "reports", label: "Reports", icon: FileText },
];

const adminNav = [...employeeNav, { id: "admin", label: "Admin", icon: UserCog }];

function CustomerView({ page, profile, accounts, amount, recipient, remarks, setAmount, setRecipient, setRemarks, transferMoney, transactions, statementDays, setStatementDays, changePassword, requestPage, setRequestPage, requests, submitServiceRequest }) {
  if (page === "profile") return <ProfilePage profile={profile} />;
  if (page === "transactions") return <TransactionsPage transactions={transactions} profile={profile} statementDays={statementDays} setStatementDays={setStatementDays} />;
  if (page === "services") return <RequestsPage profile={profile} accounts={accounts} page={requestPage} setPage={setRequestPage} requests={requests} submitServiceRequest={submitServiceRequest} />;
  if (page === "security") return <SecurityPage changePassword={changePassword} />;

  return (
    <div className="content-grid">
      <section className="dashboard-main">
        <div className="balance-band">
          <span>Available balance</span>
          <strong>{money(profile.balance)}</strong>
          <small>{profile.bankName} | {profile.accountType} | {profile.accountStatus}</small>
        </div>
        <div className="card-grid">
          <InfoCard icon={Banknote} label="Account number" value={profile.accountNumber} />
          <InfoCard icon={Building2} label="IFSC code" value={profile.ifscCode} />
          <InfoCard icon={ShieldCheck} label="Customer ID" value={profile.customerId} />
        </div>
        <section className="panel">
          <h2>Fund Transfer</h2>
          <div className="form-grid">
            <label><span>Recipient account number</span><input value={recipient} onChange={(event) => setRecipient(event.target.value)} inputMode="numeric" placeholder="10-digit RBI Bank account" /></label>
            <label><span>Amount</span><input type="number" value={amount} onChange={(event) => setAmount(event.target.value)} min="1" /></label>
            <label className="wide-field"><span>Remarks / description</span><input value={remarks} onChange={(event) => setRemarks(event.target.value)} placeholder="Purpose of transfer" /></label>
          </div>
          <div className="button-row">
            <button className="primary-button inline" onClick={transferMoney}><Send size={16} />Transfer</button>
          </div>
        </section>
      </section>
      <aside className="panel">
        <h2>Recent transactions</h2>
        <CompactTransactions rows={transactions.slice(0, 5)} />
      </aside>
    </div>
  );
}

function StaffView({ page, role, search, setSearch, staffAction, branchCashAction, requests, updateServiceRequest }) {
  const filtered = staffAccounts.filter((row) => Object.values(row).join(" ").toLowerCase().includes(search.toLowerCase()));
  if (page === "risk") return <RiskPage />;
  if (page === "reports") return <ReportsPage />;
  if (page === "admin") return <AdminPage />;
  if (page === "operations") return <BranchOperations staffAction={staffAction} branchCashAction={branchCashAction} />;
  if (page === "requests") return <StaffRequestsPage requests={requests} updateServiceRequest={updateServiceRequest} role={role} />;
  if (page === "customers") return <CustomerAccountsPage rows={filtered} search={search} setSearch={setSearch} staffAction={staffAction} requests={requests} />;

  return <StaffDashboard role={role} requests={requests} staffAction={staffAction} updateServiceRequest={updateServiceRequest} />;
}

function StaffDashboard({ role, requests, staffAction, updateServiceRequest }) {
  const submitted = requests.filter((row) => row.status === "SUBMITTED");
  const underReview = requests.filter((row) => row.status === "UNDER_REVIEW");
  const approved = requests.filter((row) => row.status === "APPROVED");
  const highValueAccounts = staffAccounts.filter((row) => row.balance >= 50000).length;
  const frozenAccounts = staffAccounts.filter((row) => row.status === "FROZEN").length;
  const todaysQueue = requests.slice(0, 4);

  return (
    <div className="staff-dashboard">
      <section className="panel wide">
        <div className="section-head">
          <div>
            <span className="eyebrow">{role === "ADMIN" ? "Branch control room" : "Employee workbench"}</span>
            <h2>Operational dashboard</h2>
          </div>
          <span className="tag pending">{submitted.length + underReview.length} open items</span>
        </div>
        <div className="ops-metrics">
          <InfoCard icon={Ticket} label="New requests" value={submitted.length} />
          <InfoCard icon={FileCheck2} label="Under review" value={underReview.length} />
          <InfoCard icon={ShieldCheck} label="Approved waiting completion" value={approved.length} />
          <InfoCard icon={ShieldAlert} label="Frozen accounts" value={frozenAccounts} />
        </div>
      </section>

      <div className="staff-layout">
        <section className="panel wide">
          <div className="section-head">
            <h2>Employee queue</h2>
            <span className="muted">{todaysQueue.length ? "Newest service requests" : "No customer requests yet"}</span>
          </div>
          <EmployeeQueueList rows={todaysQueue} updateServiceRequest={updateServiceRequest} compact />
        </section>
        <aside className="panel">
          <h2>Today at branch</h2>
          <QuickAction icon={UserPlus} title="KYC onboarding" detail={`${submitted.length} requests waiting for first review.`} />
          <QuickAction icon={Banknote} title="Cash desk readiness" detail="Deposit and withdrawal are handled from Operations." />
          <QuickAction icon={CreditCard} title="Card and FD actions" detail={`${approved.length} approved requests need completion.`} />
          <QuickAction icon={ShieldAlert} title="Risk watch" detail={`${frozenAccounts} frozen account and ${highValueAccounts} high-value accounts.`} />
          {role === "ADMIN" && <QuickAction icon={UserCog} title="Manager controls" detail="Use Admin for branch and employee governance." />}
        </aside>
      </div>
    </div>
  );
}

function CustomerAccountsPage({ rows, search, setSearch, staffAction, requests }) {
  return (
    <div className="staff-layout">
      <section className="panel wide">
        <div className="section-head">
          <div>
            <span className="eyebrow">Customer service desk</span>
            <h2>Customer accounts</h2>
          </div>
          <span className="muted">{rows.length} matching customers</span>
        </div>
        <div className="searchbar">
          <Search size={17} />
          <input placeholder="Search by name, account number, PAN, or phone" value={search} onChange={(event) => setSearch(event.target.value)} />
        </div>
        <AccountTable rows={rows} staffAction={staffAction} />
      </section>
      <aside className="panel">
        <h2>Customer actions</h2>
        <QuickAction icon={UserPlus} title="Create account" detail="Onboard only after KYC and branch approval." />
        <QuickAction icon={FileCheck2} title="KYC verification" detail="Check PAN, Aadhaar, address, and phone details." />
        <QuickAction icon={ShieldAlert} title="Freeze or activate" detail="Change account status after proper review." />
        <QuickAction icon={Ticket} title="Related requests" detail={`${requests.filter((row) => row.status !== "COMPLETED" && row.status !== "REJECTED").length} open customer requests.`} />
      </aside>
    </div>
  );
}

function BranchOperations({ staffAction, branchCashAction }) {
  const [form, setForm] = useState({ accountNumber: "", amount: "" });

  return (
    <div className="staff-layout">
      <section className="panel wide">
        <h2>Branch cash operations</h2>
        <div className="form-grid">
          <label><span>Customer account number</span><input value={form.accountNumber} onChange={(event) => setForm({ ...form, accountNumber: event.target.value })} inputMode="numeric" /></label>
          <label><span>Amount</span><input type="number" min="1" value={form.amount} onChange={(event) => setForm({ ...form, amount: event.target.value })} /></label>
        </div>
        <div className="button-row">
          <button onClick={() => staffAction("Cash deposit", () => branchCashAction("deposit", form.accountNumber, form.amount))}><BadgeIndianRupee size={16} />Deposit to account</button>
          <button onClick={() => staffAction("Cash withdrawal", () => branchCashAction("withdraw", form.accountNumber, form.amount))}><Banknote size={16} />Withdraw from account</button>
        </div>
      </section>
      <aside className="panel">
        <h2>Controls</h2>
        <QuickAction icon={FileCheck2} title="KYC verification" detail="Verify identity before enabling full account operations." />
        <QuickAction icon={ShieldAlert} title="Freeze or unfreeze" detail="Use account status changes for fraud, legal, or KYC holds." />
        <QuickAction icon={FileText} title="Transaction log" detail="Review all branch cash entries and internal transfers." />
      </aside>
    </div>
  );
}

function ProfilePage({ profile }) {
  return (
    <section className="panel wide">
      <h2>Customer profile</h2>
      <div className="detail-grid">
        {Object.entries(profile).map(([key, value]) => (
          <div key={key}>
            <span>{labelize(key)}</span>
            <strong>{key === "balance" ? money(value) : value}</strong>
          </div>
        ))}
      </div>
    </section>
  );
}

function TransactionsPage({ transactions, profile, statementDays, setStatementDays }) {
  const [filters, setFilters] = useState({ q: "", type: "ALL", from: "", to: "" });
  const filtered = useMemo(() => filterTransactions(transactions, filters), [transactions, filters]);

  return (
    <section className="panel wide">
      <div className="section-head">
        <h2>Transactions</h2>
        <div className="button-row">
          <select value={statementDays} onChange={(event) => setStatementDays(event.target.value)}>
            <option value="7">Last 7 days</option>
            <option value="15">Last 15 days</option>
            <option value="30">Last 30 days</option>
          </select>
          <button onClick={() => downloadMiniStatement(profile, filtered, statementDays)}><Download size={16} />Mini statement</button>
        </div>
      </div>
      <div className="filter-grid">
        <label><span>Search</span><input placeholder="Transaction ID, account, or name" value={filters.q} onChange={(event) => setFilters({ ...filters, q: event.target.value })} /></label>
        <label><span>Type</span><select value={filters.type} onChange={(event) => setFilters({ ...filters, type: event.target.value })}><option>ALL</option><option>CREDIT</option><option>DEBIT</option><option>TRANSFER</option></select></label>
        <label><span>From date</span><input type="date" value={filters.from} onChange={(event) => setFilters({ ...filters, from: event.target.value })} /></label>
        <label><span>To date</span><input type="date" value={filters.to} onChange={(event) => setFilters({ ...filters, to: event.target.value })} /></label>
      </div>
      <TransactionTable rows={filtered} />
    </section>
  );
}

const requestServices = [
  { id: "cheque-book", type: "CHEQUE_BOOK", title: "Cheque Book", owner: "Branch Operations", time: "3-5 working days", detail: "Request cheque leaves for an active savings or current account." },
  { id: "fixed-deposit", type: "FIXED_DEPOSIT", title: "Fixed Deposit", owner: "Deposits Desk", time: "Same day review", detail: "Create an FD request with nominee, payout, maturity, and renewal details." },
  { id: "loan", type: "LOAN", title: "Loan Application", owner: "Credit Team", time: "2-4 working days", detail: "Apply for personal, home, education, or vehicle loan pre-screening." },
  { id: "support-ticket", type: "SUPPORT_TICKET", title: "Support Ticket", owner: "Customer Care", time: "24-48 hours", detail: "Raise transaction, login, card, loan, fraud, or general service issues." },
  { id: "block-debit-card", type: "BLOCK_DEBIT_CARD", title: "Block Debit Card", owner: "Card Control", time: "Immediate action", detail: "Block a lost, stolen, damaged, or suspicious debit card." },
];

function RequestsPage({ profile, accounts, page, setPage, requests, submitServiceRequest }) {
  const accountOptions = accounts.length ? accounts : [{ accountNumber: profile.accountNumber, accountStatus: profile.accountStatus, balance: profile.balance }];
  if (page === "history") return <RequestHistoryPage requests={requests} onBack={() => setPage("hub")} />;
  const selectedService = requestServices.find((service) => service.id === page);
  if (selectedService) {
    return <RequestServicePage service={selectedService} profile={profile} accounts={accountOptions} onBack={() => setPage("hub")} submitServiceRequest={submitServiceRequest} />;
  }

  return (
    <div className="service-space">
      <section className="panel wide service-hero">
        <div>
          <span className="eyebrow">Service Requests</span>
          <h2>RBI Bank service hub</h2>
          <p className="muted">Choose a service, complete the dedicated banking form, confirm the request, and track status from submission to completion.</p>
        </div>
        <button onClick={() => setPage("history")}><BookOpen size={16} />Request history</button>
      </section>
      <div className="service-grid">
        {requestServices.map((service) => (
          <section className="service-card" key={service.id}>
            <div className="service-card-head">
              <Ticket size={20} />
              <span>{service.owner}</span>
            </div>
            <h3>{service.title}</h3>
            <p>{service.detail}</p>
            <div className="service-meta"><span>{service.time}</span><span>RBI Bank internal workflow</span></div>
            <button className="primary-button inline" onClick={() => setPage(service.id)}>Apply</button>
          </section>
        ))}
      </div>
    </div>
  );
}

function RequestServicePage({ service, profile, accounts, onBack, submitServiceRequest }) {
  if (service.type === "CHEQUE_BOOK") return <ChequeBookRequest service={service} profile={profile} accounts={accounts} onBack={onBack} submitServiceRequest={submitServiceRequest} />;
  if (service.type === "FIXED_DEPOSIT") return <FixedDepositRequest service={service} accounts={accounts} onBack={onBack} submitServiceRequest={submitServiceRequest} />;
  if (service.type === "LOAN") return <LoanRequest service={service} profile={profile} onBack={onBack} submitServiceRequest={submitServiceRequest} />;
  if (service.type === "SUPPORT_TICKET") return <SupportTicketRequest service={service} onBack={onBack} submitServiceRequest={submitServiceRequest} />;
  return <BlockCardRequest service={service} profile={profile} onBack={onBack} submitServiceRequest={submitServiceRequest} />;
}

function RequestFormShell({ service, onBack, children, summary, status }) {
  return (
    <div className="request-layout">
      <section className="panel wide">
        <div className="section-head">
          <div>
            <span className="eyebrow">{service.owner}</span>
            <h2>{service.title}</h2>
          </div>
          <button onClick={onBack}>Back to hub</button>
        </div>
        <div className="step-row">
          {["Fill details", "Confirm", "Submitted", "Review"].map((step, index) => <span key={step} className={index === 0 ? "active-step" : ""}>{step}</span>)}
        </div>
        {children}
      </section>
      <aside className="panel">
        <h2>Request summary</h2>
        {summary}
        <StatusTimeline current={status} />
      </aside>
    </div>
  );
}

function ChequeBookRequest({ service, profile, accounts, onBack, submitServiceRequest }) {
  const [form, setForm] = useState({ accountNumber: String(accounts[0]?.accountNumber || ""), leaves: "25", deliveryMethod: "Registered address", communicationPreference: "SMS", reason: "", confirmAddress: profile.address });
  return (
    <ServiceRequestForm service={service} form={form} setForm={setForm} onBack={onBack} submitServiceRequest={submitServiceRequest} validate={() => !form.reason || !form.confirmAddress ? "Reason and confirmed address are required" : ""} summary={<SummaryRows rows={[["Account", form.accountNumber], ["Leaves", form.leaves], ["Delivery", form.deliveryMethod], ["Address", form.confirmAddress]]} />}>
      <div className="form-grid">
        <SelectField label="Select account number" value={form.accountNumber} onChange={(v) => setForm({ ...form, accountNumber: v })} options={accounts.map((account) => String(account.accountNumber))} />
        <SelectField label="Number of cheque leaves" value={form.leaves} onChange={(v) => setForm({ ...form, leaves: v })} options={["25", "50", "100"]} />
        <SelectField label="Delivery method" value={form.deliveryMethod} onChange={(v) => setForm({ ...form, deliveryMethod: v })} options={["Registered address", "Branch pickup"]} />
        <SelectField label="Communication preference" value={form.communicationPreference} onChange={(v) => setForm({ ...form, communicationPreference: v })} options={["SMS", "Email"]} />
        <label className="wide-field"><span>Reason for request</span><input value={form.reason} onChange={(event) => setForm({ ...form, reason: event.target.value })} /></label>
        <label className="wide-field"><span>Confirm address</span><input value={form.confirmAddress} onChange={(event) => setForm({ ...form, confirmAddress: event.target.value })} /></label>
      </div>
    </ServiceRequestForm>
  );
}

function FixedDepositRequest({ service, accounts, onBack, submitServiceRequest }) {
  const [form, setForm] = useState({ sourceAccount: String(accounts[0]?.accountNumber || ""), amount: "10000", tenure: "1 year", payoutType: "On maturity", nomineeName: "", nomineeRelationship: "", autoRenewal: "Yes", termsAccepted: false });
  const months = { "6 months": 6, "1 year": 12, "3 years": 36, "5 years": 60 }[form.tenure];
  const rate = months >= 36 ? 7.1 : months >= 12 ? 6.8 : 6.25;
  const maturityAmount = Number(form.amount || 0) * (1 + (rate / 100) * (months / 12));
  const maturityDate = addMonths(new Date(), months).toLocaleDateString("en-IN");
  return (
    <ServiceRequestForm service={service} form={{ ...form, interestRate: `${rate}%`, estimatedMaturityAmount: Math.round(maturityAmount), maturityDate }} setForm={setForm} onBack={onBack} submitServiceRequest={submitServiceRequest} validate={() => Number(form.amount) < 1000 ? "FD amount must be at least INR 1,000" : !form.nomineeName || !form.nomineeRelationship ? "Nominee details are required" : !form.termsAccepted ? "Accept terms and conditions" : ""} summary={<SummaryRows rows={[["Interest rate", `${rate}%`], ["Maturity amount", money(maturityAmount)], ["Maturity date", maturityDate], ["Auto renewal", form.autoRenewal]]} />}>
      <div className="form-grid">
        <SelectField label="Source account number" value={form.sourceAccount} onChange={(v) => setForm({ ...form, sourceAccount: v })} options={accounts.map((account) => String(account.accountNumber))} />
        <label><span>FD amount</span><input type="number" value={form.amount} onChange={(event) => setForm({ ...form, amount: event.target.value })} /></label>
        <SelectField label="FD tenure" value={form.tenure} onChange={(v) => setForm({ ...form, tenure: v })} options={["6 months", "1 year", "3 years", "5 years"]} />
        <SelectField label="Interest payout type" value={form.payoutType} onChange={(v) => setForm({ ...form, payoutType: v })} options={["Monthly", "Quarterly", "On maturity"]} />
        <label><span>Nominee name</span><input value={form.nomineeName} onChange={(event) => setForm({ ...form, nomineeName: event.target.value })} /></label>
        <label><span>Nominee relationship</span><input value={form.nomineeRelationship} onChange={(event) => setForm({ ...form, nomineeRelationship: event.target.value })} /></label>
        <SelectField label="Auto-renewal option" value={form.autoRenewal} onChange={(v) => setForm({ ...form, autoRenewal: v })} options={["Yes", "No"]} />
        <label className="checkbox-line"><input type="checkbox" checked={form.termsAccepted} onChange={(event) => setForm({ ...form, termsAccepted: event.target.checked })} />I accept FD terms and premature closure rules</label>
      </div>
    </ServiceRequestForm>
  );
}

function LoanRequest({ service, profile, onBack, submitServiceRequest }) {
  const [form, setForm] = useState({ loanType: "Personal loan", amount: "200000", monthlyIncome: "60000", employmentType: "Salaried", employerName: "", existingEmis: "0", panNumber: profile.panNumber, aadhaarNumber: profile.aadhaarNumber, purpose: "", tenure: "36 months", documents: "Salary slips, PAN, Aadhaar, bank statement" });
  const rate = form.loanType === "Home loan" ? 8.6 : form.loanType === "Education loan" ? 9.2 : form.loanType === "Vehicle loan" ? 9.8 : 11.5;
  const emi = calculateEmi(Number(form.amount), rate, Number(form.tenure.split(" ")[0]));
  const eligible = Number(form.monthlyIncome || 0) - Number(form.existingEmis || 0) > emi * 2;
  return (
    <ServiceRequestForm service={service} form={{ ...form, interestRate: `${rate}%`, estimatedEmi: Math.round(emi), eligibilityStatus: eligible ? "Likely eligible" : "Manual review required" }} setForm={setForm} onBack={onBack} submitServiceRequest={submitServiceRequest} validate={() => !form.employerName || !form.purpose ? "Employer and loan purpose are required" : Number(form.amount) < 50000 ? "Requested amount must be at least INR 50,000" : ""} summary={<SummaryRows rows={[["Interest rate", `${rate}%`], ["Estimated EMI", money(emi)], ["Eligibility", eligible ? "Likely eligible" : "Manual review"], ["Documents", "UI capture only"]]} />}>
      <div className="form-grid">
        <SelectField label="Loan type" value={form.loanType} onChange={(v) => setForm({ ...form, loanType: v })} options={["Personal loan", "Home loan", "Education loan", "Vehicle loan"]} />
        <label><span>Requested amount</span><input type="number" value={form.amount} onChange={(event) => setForm({ ...form, amount: event.target.value })} /></label>
        <label><span>Monthly income</span><input type="number" value={form.monthlyIncome} onChange={(event) => setForm({ ...form, monthlyIncome: event.target.value })} /></label>
        <SelectField label="Employment type" value={form.employmentType} onChange={(v) => setForm({ ...form, employmentType: v })} options={["Salaried", "Self-employed", "Student with co-applicant", "Retired"]} />
        <label><span>Employer / company name</span><input value={form.employerName} onChange={(event) => setForm({ ...form, employerName: event.target.value })} /></label>
        <label><span>Existing EMIs</span><input type="number" value={form.existingEmis} onChange={(event) => setForm({ ...form, existingEmis: event.target.value })} /></label>
        <label><span>PAN number</span><input value={form.panNumber} onChange={(event) => setForm({ ...form, panNumber: event.target.value })} /></label>
        <label><span>Aadhaar number</span><input value={form.aadhaarNumber} onChange={(event) => setForm({ ...form, aadhaarNumber: event.target.value })} /></label>
        <SelectField label="Loan tenure" value={form.tenure} onChange={(v) => setForm({ ...form, tenure: v })} options={["12 months", "24 months", "36 months", "60 months", "120 months"]} />
        <label className="wide-field"><span>Purpose of loan</span><input value={form.purpose} onChange={(event) => setForm({ ...form, purpose: event.target.value })} /></label>
        <label className="wide-field"><span>Upload documents section</span><input value={form.documents} onChange={(event) => setForm({ ...form, documents: event.target.value })} /></label>
      </div>
    </ServiceRequestForm>
  );
}

function SupportTicketRequest({ service, onBack, submitServiceRequest }) {
  const [form, setForm] = useState({ category: "Transaction issue", subject: "", description: "", transactionId: "", contactMethod: "Phone" });
  const department = form.category === "Fraud complaint" ? "Fraud Risk Cell" : form.category.includes("Debit") ? "Card Services" : form.category.includes("Loan") ? "Loan Servicing" : "Customer Support";
  const needsTransactionId = form.category === "Transaction issue" || form.category === "Fraud complaint";
  return (
    <ServiceRequestForm service={service} form={{ ...form, assignedDepartment: department, transactionId: needsTransactionId ? form.transactionId : "" }} setForm={setForm} onBack={onBack} submitServiceRequest={submitServiceRequest} validate={() => !form.subject || form.description.length < 20 ? "Subject and at least 20 characters of description are required" : ""} summary={<SummaryRows rows={[["Category", form.category], ["Department", department], ["Contact", form.contactMethod], ...(needsTransactionId ? [["Transaction ID", form.transactionId || "Not provided"]] : [])]} />}>
      <div className="form-grid">
        <SelectField label="Issue category" value={form.category} onChange={(v) => setForm({ ...form, category: v, transactionId: v === "Transaction issue" || v === "Fraud complaint" ? form.transactionId : "" })} options={["Transaction issue", "Login issue", "Debit card issue", "Loan issue", "Fraud complaint", "Other"]} />
        <SelectField label="Preferred contact method" value={form.contactMethod} onChange={(v) => setForm({ ...form, contactMethod: v })} options={["Phone", "SMS", "Email"]} />
        <label><span>Subject</span><input value={form.subject} onChange={(event) => setForm({ ...form, subject: event.target.value })} /></label>
        {needsTransactionId && <label><span>Transaction ID (optional)</span><input value={form.transactionId} onChange={(event) => setForm({ ...form, transactionId: event.target.value })} /></label>}
        <label className="wide-field"><span>Detailed description</span><textarea value={form.description} onChange={(event) => setForm({ ...form, description: event.target.value })} /></label>
      </div>
    </ServiceRequestForm>
  );
}

function BlockCardRequest({ service, profile, onBack, submitServiceRequest }) {
  const [form, setForm] = useState({ atmCardNumber: "", reason: "Lost", blockType: "Permanent block", occurrenceDate: new Date().toISOString().slice(0, 10), comments: "" });
  const enteredCard = digitsOnly(form.atmCardNumber);
  const actualCard = digitsOnly(profile.atmCardNumber);
  return (
    <ServiceRequestForm service={service} form={{ ...form, atmCardNumber: enteredCard, matchedCardNumber: actualCard }} setForm={setForm} onBack={onBack} submitServiceRequest={submitServiceRequest} validate={() => !form.occurrenceDate ? "Date of issue occurrence is required" : enteredCard.length !== 16 ? "Enter a valid 16-digit ATM card number" : enteredCard !== actualCard ? "ATM card number does not match your profile. Request paused." : ""} summary={<SummaryRows rows={[["ATM card", enteredCard ? maskCard(enteredCard) : "Not entered"], ["Reason", form.reason], ["Block type", form.blockType], ["Occurrence", form.occurrenceDate]]} />}>
      <div className="warning-strip">Blocking card will stop all ATM, POS, and online card transactions immediately.</div>
      <div className="form-grid">
        <label><span>ATM / debit card number</span><input value={form.atmCardNumber} onChange={(event) => setForm({ ...form, atmCardNumber: event.target.value.replace(/\D/g, "").slice(0, 16) })} inputMode="numeric" placeholder="Enter 16-digit card number" /></label>
        <SelectField label="Reason for blocking" value={form.reason} onChange={(v) => setForm({ ...form, reason: v })} options={["Lost", "Stolen", "Damaged", "Fraud suspicion"]} />
        <SelectField label="Block type" value={form.blockType} onChange={(v) => setForm({ ...form, blockType: v })} options={["Permanent block", "Temporary block"]} />
        <label><span>Date of issue occurrence</span><input type="date" value={form.occurrenceDate} onChange={(event) => setForm({ ...form, occurrenceDate: event.target.value })} /></label>
        <label className="wide-field"><span>Additional comments</span><input value={form.comments} onChange={(event) => setForm({ ...form, comments: event.target.value })} /></label>
      </div>
    </ServiceRequestForm>
  );
}

function ServiceRequestForm({ service, form, setForm, onBack, submitServiceRequest, validate, summary, children }) {
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [submitted, setSubmitted] = useState(null);
  const [error, setError] = useState("");

  async function submit() {
    const validationError = validate?.();
    if (validationError) {
      setError(validationError);
      return;
    }
    setError("");
    setConfirmOpen(true);
  }

  async function confirmSubmit() {
    try {
      const request = await submitServiceRequest(service.type, form);
      setSubmitted(request);
      setConfirmOpen(false);
    } catch (submitError) {
      setError(submitError.message);
      setConfirmOpen(false);
    }
  }

  return (
    <RequestFormShell service={service} onBack={onBack} summary={summary} status={submitted?.status || "SUBMITTED"}>
      {children}
      <div className="button-row">
        <button className="primary-button inline" onClick={submit}><Ticket size={16} />Submit request</button>
        <button onClick={() => setForm({ ...form })}>Save draft</button>
      </div>
      {error && <p className="form-message danger">{error}</p>}
      {submitted && (
        <div className="success-panel">
          <strong>{submitted.requestId} submitted successfully</strong>
          <span>Status: {titleCase(submitted.status)}. Bank staff will review and update the request history.</span>
        </div>
      )}
      {confirmOpen && <ConfirmModal title={`Confirm ${service.title}`} onCancel={() => setConfirmOpen(false)} onConfirm={confirmSubmit} />}
    </RequestFormShell>
  );
}

function ConfirmModal({ title, onCancel, onConfirm }) {
  return (
    <div className="modal-backdrop">
      <section className="modal">
        <h2>{title}</h2>
        <p className="muted">Please confirm that the details entered are correct. Once submitted, the request will be visible to bank staff for review.</p>
        <div className="button-row">
          <button onClick={onCancel}>Review again</button>
          <button className="primary-button inline" onClick={onConfirm}>Confirm submit</button>
        </div>
      </section>
    </div>
  );
}

function RequestHistoryPage({ requests, onBack }) {
  return (
    <section className="panel wide">
      <div className="section-head">
        <h2>Request history</h2>
        <button onClick={onBack}>Back to services</button>
      </div>
      <RequestTable rows={requests} />
    </section>
  );
}

function StaffRequestsPage({ requests, updateServiceRequest, role }) {
  const [filters, setFilters] = useState({ q: "", status: "ALL", type: "ALL" });
  const filtered = requests.filter((row) => {
    const details = parseRequestData(row.requestData);
    const haystack = [row.requestId, row.requestType, row.status, details.subject, details.accountNumber, details.loanType, details.category].join(" ").toLowerCase();
    return (filters.status === "ALL" || row.status === filters.status)
      && (filters.type === "ALL" || row.requestType === filters.type)
      && (!filters.q || haystack.includes(filters.q.toLowerCase()));
  });

  return (
    <section className="panel wide">
      <div className="section-head">
        <div>
          <span className="eyebrow">{role === "ADMIN" ? "Manager monitoring" : "Employee work queue"}</span>
          <h2>Service request management</h2>
        </div>
      </div>
      <div className="filter-grid">
        <label><span>Search</span><input placeholder="Request ID, type, customer detail" value={filters.q} onChange={(event) => setFilters({ ...filters, q: event.target.value })} /></label>
        <SelectField label="Status" value={filters.status} onChange={(v) => setFilters({ ...filters, status: v })} options={["ALL", "SUBMITTED", "UNDER_REVIEW", "APPROVED", "REJECTED", "COMPLETED"]} />
        <SelectField label="Type" value={filters.type} onChange={(v) => setFilters({ ...filters, type: v })} options={["ALL", ...requestServices.map((service) => service.type)]} />
      </div>
      <EmployeeQueueList rows={filtered} updateServiceRequest={updateServiceRequest} />
    </section>
  );
}

function EmployeeQueueList({ rows, updateServiceRequest, compact = false }) {
  const [remarks, setRemarks] = useState({});
  if (!rows.length) {
    return <div className="empty-state"><strong>No queue items</strong><span>New customer service requests will appear here after submission.</span></div>;
  }

  return (
    <div className="request-admin-list">
      {rows.map((row) => {
        const details = parseRequestData(row.requestData);
        const defaultRemark = queueDefaultRemark(row.status);
        return (
          <section className={`request-admin-card ${compact ? "compact" : ""}`} key={row.id}>
            <div>
              <strong>{row.requestId}</strong>
              <span>{labelize(row.requestType.toLowerCase())} | {formatDateTime(row.createdAt)}</span>
              <p>{summarizeRequest(row, details)}</p>
              {row.reviewedBy && row.reviewedBy !== "Pending" && <p>Reviewed by {row.reviewedBy}</p>}
            </div>
            <span className={`tag ${requestStatusTone(row.status)}`}>{titleCase(row.status.replace("_", " "))}</span>
            {!compact && <label><span>Remarks</span><input value={remarks[row.id] || ""} onChange={(event) => setRemarks({ ...remarks, [row.id]: event.target.value })} placeholder="Approval or rejection remarks" /></label>}
            <div className="button-row">
              {row.status === "SUBMITTED" && <button onClick={() => updateServiceRequest(row.id, "UNDER_REVIEW", remarks[row.id] || defaultRemark)}>Review</button>}
              {row.status !== "APPROVED" && row.status !== "COMPLETED" && row.status !== "REJECTED" && <button onClick={() => updateServiceRequest(row.id, "APPROVED", remarks[row.id] || "Approved as per bank policy")}>Approve</button>}
              {row.status !== "REJECTED" && row.status !== "COMPLETED" && <button onClick={() => updateServiceRequest(row.id, "REJECTED", remarks[row.id] || "Rejected after verification")}>Reject</button>}
              {row.status === "APPROVED" && <button className="primary-button inline" onClick={() => updateServiceRequest(row.id, "COMPLETED", remarks[row.id] || "Request completed")}>Complete</button>}
            </div>
          </section>
        );
      })}
    </div>
  );
}

function RequestTable({ rows }) {
  return (
    <table>
      <thead><tr><th>Request ID</th><th>Type</th><th>Submitted</th><th>Status</th><th>Reviewed by</th><th>Approval remarks</th><th>Summary</th></tr></thead>
      <tbody>
        {rows.map((row) => {
          const details = parseRequestData(row.requestData);
          return (
            <tr key={row.requestId}>
              <td className="mono">{row.requestId}</td>
              <td>{labelize(row.requestType.toLowerCase())}</td>
              <td>{formatDateTime(row.createdAt)}</td>
              <td><span className={`tag ${requestStatusTone(row.status)}`}>{titleCase(row.status.replace("_", " "))}</span></td>
              <td>{row.reviewedBy || "Pending"}</td>
              <td>{row.approvalRemarks || "-"}</td>
              <td>{summarizeRequest(row, details)}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}

function SelectField({ label, value, onChange, options }) {
  return (
    <label>
      <span>{label}</span>
      <select value={value} onChange={(event) => onChange(event.target.value)}>
        {options.map((option) => <option key={option} value={option}>{option}</option>)}
      </select>
    </label>
  );
}

function SummaryRows({ rows }) {
  return <div className="summary-list">{rows.map(([label, value]) => <div key={label}><span>{label}</span><strong>{value || "-"}</strong></div>)}</div>;
}

function StatusTimeline({ current }) {
  const steps = ["SUBMITTED", "UNDER_REVIEW", "APPROVED", "COMPLETED"];
  const currentIndex = Math.max(0, steps.indexOf(current));
  return (
    <div className="status-timeline">
      {steps.map((step, index) => <div key={step} className={index <= currentIndex ? "done" : ""}><span />{titleCase(step.replace("_", " "))}</div>)}
    </div>
  );
}

function SecurityPage({ changePassword }) {
  const [form, setForm] = useState({ currentPassword: "", newPassword: "" });
  const [status, setStatus] = useState({ tone: "", text: "" });
  const [saving, setSaving] = useState(false);

  async function submitPasswordChange(event) {
    event.preventDefault();
    setSaving(true);
    setStatus({ tone: "", text: "Changing password..." });
    try {
      const data = await changePassword(form.currentPassword, form.newPassword);
      setForm({ currentPassword: "", newPassword: "" });
      setStatus({ tone: "success", text: data?.message || "Password changed successfully" });
    } catch (error) {
      setStatus({ tone: "danger", text: error.message || "Unable to change password" });
    } finally {
      setSaving(false);
    }
  }

  return (
    <form className="panel wide" onSubmit={submitPasswordChange}>
      <h2>Password and access</h2>
      <div className="form-grid">
        <label><span>Current password</span><input type="password" value={form.currentPassword} onChange={(event) => setForm({ ...form, currentPassword: event.target.value })} autoComplete="current-password" /></label>
        <label><span>New password</span><input type="password" value={form.newPassword} onChange={(event) => setForm({ ...form, newPassword: event.target.value })} autoComplete="new-password" /></label>
      </div>
      <button className="primary-button inline" type="submit" disabled={saving}><Lock size={16} />{saving ? "Changing..." : "Change password"}</button>
      {status.text && <p className={`form-message ${status.tone}`}>{status.text}</p>}
    </form>
  );
}

function RiskPage() {
  return (
    <section className="panel wide">
      <h2>Suspicious transactions</h2>
      <TransactionTable rows={demoTransactions.filter((tx) => tx.status === "Review")} />
    </section>
  );
}

function ReportsPage() {
  return (
    <div className="card-grid">
      <InfoCard icon={Wallet} label="Customer accounts" value="3" />
      <InfoCard icon={BadgeIndianRupee} label="Total deposits" value="INR 173,400" />
      <InfoCard icon={ShieldAlert} label="Frozen accounts" value="1" />
    </div>
  );
}

function AdminPage() {
  return (
    <section className="panel wide">
      <h2>Admin controls</h2>
      <div className="admin-grid">
        <QuickAction icon={Building2} title="Manage branches" detail="Create branch records and assign IFSC codes." />
        <QuickAction icon={UserCog} title="Manage employees" detail="Create staff users and manager permissions." />
        <QuickAction icon={FileText} title="Audit logs" detail="Review customer, employee, and transaction changes." />
      </div>
    </section>
  );
}

function AccountTable({ rows, staffAction }) {
  return (
    <table>
      <thead><tr><th>Name</th><th>Account</th><th>PAN</th><th>Phone</th><th>Balance</th><th>Status</th><th>Action</th></tr></thead>
      <tbody>
        {rows.map((row) => (
          <tr key={row.accountNumber}>
            <td>{row.name}</td>
            <td>{row.accountNumber}</td>
            <td>{row.pan}</td>
            <td>{row.phone}</td>
            <td>{money(row.balance)}</td>
            <td><span className={`tag ${row.status.toLowerCase()}`}>{row.status}</span></td>
            <td><button onClick={() => staffAction("Account review")}><ShieldCheck size={15} />Review</button></td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function TransactionTable({ rows }) {
  return (
    <table>
      <thead>
        <tr>
          <th>Transaction ID</th>
          <th>Date and time</th>
          <th>Type</th>
          <th>Amount</th>
          <th>Status</th>
          <th>Sender account</th>
          <th>Receiver account</th>
          <th>Customer / beneficiary</th>
          <th>Remarks</th>
          <th>Balance after</th>
        </tr>
      </thead>
      <tbody>
        {rows.map((row) => (
          <tr key={row.txId}>
            <td className="mono">{row.txId}</td>
            <td>{formatDateTime(row.createdAt)}</td>
            <td><span className={`tag ${transactionTone(row.type)}`}>{normalizeType(row.type)}</span></td>
            <td className={transactionTone(row.type)}>{money(row.amount)}</td>
            <td><span className={`tag ${statusTone(row.status)}`}>{normalizeStatus(row.status)}</span></td>
            <td>{row.senderAccountNumber || "-"}</td>
            <td>{row.receiverAccountNumber || "-"}</td>
            <td><strong className="counterparty">{row.counterpartyName || "-"}</strong></td>
            <td>{row.description || "-"}</td>
            <td>{row.balanceAfterTransaction == null ? "-" : money(row.balanceAfterTransaction)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function CompactTransactions({ rows }) {
  return <div className="mini-list">{rows.map((row) => <div key={row.txId}><strong>{normalizeType(row.type)} - {row.counterpartyName || "RBI Bank"}</strong><span>{formatDateTime(row.createdAt)} | {normalizeStatus(row.status)}</span><b className={transactionTone(row.type)}>{money(row.amount)}</b></div>)}</div>;
}

function InfoCard({ icon: Icon, label, value }) {
  return <section className="info-card"><Icon size={20} /><span>{label}</span><strong>{value}</strong></section>;
}

function QuickAction({ icon: Icon, title, detail }) {
  return <div className="quick-action"><Icon size={18} /><div><strong>{title}</strong><span>{detail}</span></div></div>;
}

function buildProfile(user, accounts, card) {
  const account = accounts[0];
  return {
    ...demoProfile,
    customerId: user?.customerId || demoProfile.customerId,
    accountNumber: account?.accountNumber || demoProfile.accountNumber,
    ifscCode: account?.ifscCode || demoProfile.ifscCode,
    accountType: titleCase(account?.accountType || demoProfile.accountType),
    accountStatus: titleCase(account?.accountStatus || demoProfile.accountStatus),
    balance: account?.balance ?? demoProfile.balance,
    lastLoginTime: new Date().toLocaleString("en-IN"),
    lastTransactionDate: account?.lastTransactionAt ? formatDateTime(account.lastTransactionAt) : "No recent transaction",
    panNumber: user?.panNumber || demoProfile.panNumber,
    aadhaarNumber: maskAadhaar(user?.aadhaarNumber || demoProfile.aadhaarNumber),
    address: user?.address || demoProfile.address,
    contact: `${user?.email || "customer@rbibank.local"} | ${user?.tel || "9876543210"}`,
    atmCardNumber: card?.cardNumber ? String(card.cardNumber) : demoAtmCardNumber(user?.username),
  };
}

function money(value) {
  return `INR ${Number(value || 0).toLocaleString("en-IN", { maximumFractionDigits: 2 })}`;
}

function titleCase(value) {
  return String(value || "").toLowerCase().replace(/\b\w/g, (char) => char.toUpperCase());
}

function labelize(key) {
  return key.replace(/([A-Z])/g, " $1").replace(/^./, (char) => char.toUpperCase());
}

function maskAadhaar(value) {
  const digits = String(value || "").replace(/\D/g, "");
  if (digits.length < 4) return value;
  return `XXXX XXXX ${digits.slice(-4)}`;
}

function digitsOnly(value) {
  return String(value || "").replace(/\D/g, "");
}

function maskCard(value) {
  const digits = digitsOnly(value);
  if (digits.length < 4) return value || "-";
  return `XXXX XXXX XXXX ${digits.slice(-4)}`;
}

function demoAtmCardNumber(username) {
  const cards = {
    customer: "4213768450129012",
    nisha: "4213768450124821",
    rahul: "4213768450127436",
    priya: "4213768450125724",
  };
  return cards[username] || demoProfile.atmCardNumber;
}

function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString("en-IN", { dateStyle: "medium", timeStyle: "short" });
}

function normalizeType(value) {
  if (value === "WITHDRAW") return "DEBIT";
  if (value === "DEPOSIT") return "CREDIT";
  return String(value || "TRANSFER").toUpperCase();
}

function normalizeStatus(value) {
  if (value === "COMPLETED") return "SUCCESS";
  return String(value || "PENDING").toUpperCase();
}

function transactionTone(value) {
  const type = normalizeType(value);
  if (type === "CREDIT") return "success";
  if (type === "DEBIT" || type === "FAILED") return "danger";
  return "pending";
}

function statusTone(value) {
  const status = normalizeStatus(value);
  if (status === "SUCCESS") return "success";
  if (status === "FAILED") return "danger";
  return "pending";
}

function filterTransactions(rows, filters) {
  const query = filters.q.trim().toLowerCase();
  return rows.filter((row) => {
    const typeMatches = filters.type === "ALL" || normalizeType(row.type) === filters.type;
    const date = row.createdAt ? new Date(row.createdAt) : null;
    const afterFrom = !filters.from || (date && date >= new Date(`${filters.from}T00:00:00`));
    const beforeTo = !filters.to || (date && date <= new Date(`${filters.to}T23:59:59`));
    const haystack = [
      row.txId,
      row.senderAccountNumber,
      row.receiverAccountNumber,
      row.counterpartyName,
      row.description,
    ].join(" ").toLowerCase();
    return typeMatches && afterFrom && beforeTo && (!query || haystack.includes(query));
  });
}

function parseRequestData(value) {
  try {
    return value ? JSON.parse(value) : {};
  } catch {
    return {};
  }
}

function summarizeRequest(row, details) {
  if (row.requestType === "CHEQUE_BOOK") return `${details.leaves || "-"} leaves for account ${details.accountNumber || "-"}`;
  if (row.requestType === "FIXED_DEPOSIT") return `${money(details.amount)} for ${details.tenure || "-"}, maturity ${details.maturityDate || "-"}`;
  if (row.requestType === "LOAN") return `${details.loanType || "Loan"} ${money(details.amount)} | EMI ${money(details.estimatedEmi)}`;
  if (row.requestType === "SUPPORT_TICKET") return `${details.category || "Support"} - ${details.subject || "-"}`;
  if (row.requestType === "BLOCK_DEBIT_CARD") return `${details.blockType || "Block"} for ${details.atmCardNumber ? maskCard(details.atmCardNumber) : "-"}`;
  return "Service request";
}

function queueDefaultRemark(status) {
  if (status === "SUBMITTED") return "Request taken for employee review";
  if (status === "UNDER_REVIEW") return "Verification completed by branch staff";
  if (status === "APPROVED") return "Request completed";
  return "Updated by branch staff";
}

function requestStatusTone(status) {
  if (status === "APPROVED" || status === "COMPLETED") return "success";
  if (status === "REJECTED") return "danger";
  return "pending";
}

function addMonths(date, months) {
  const next = new Date(date);
  next.setMonth(next.getMonth() + months);
  return next;
}

function calculateEmi(principal, annualRate, months) {
  if (!principal || !months) return 0;
  const monthlyRate = annualRate / 12 / 100;
  return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
}

function downloadMiniStatement(profile, transactions, days) {
  const periodStart = new Date();
  periodStart.setDate(periodStart.getDate() - Number(days));
  const rows = transactions.filter((tx) => {
    const date = new Date(tx.createdAt);
    return !Number.isNaN(date.getTime()) && date >= periodStart;
  });
  const openingBalance = rows.length ? rows[rows.length - 1].balanceAfterTransaction || profile.balance : profile.balance;
  const printWindow = window.open("", "_blank", "width=900,height=700");
  if (!printWindow) return;
  printWindow.document.write(`
    <html>
      <head>
        <title>RBI Bank Mini Statement</title>
        <style>
          body { font-family: Arial, sans-serif; color: #172033; padding: 28px; }
          h1 { margin: 0; }
          table { border-collapse: collapse; width: 100%; margin-top: 18px; font-size: 12px; }
          th, td { border-bottom: 1px solid #dce5ee; padding: 8px; text-align: left; }
          th { background: #f1f5f9; }
          .meta { color: #526174; line-height: 1.6; margin-top: 12px; }
        </style>
      </head>
      <body>
        <h1>RBI Bank</h1>
        <div class="meta">
          Customer: ${profile.customerId}<br />
          Account: ${profile.accountNumber}<br />
          Statement period: Last ${days} days<br />
          Opening balance: ${money(openingBalance)}<br />
          Closing balance: ${money(profile.balance)}<br />
          Generated: ${new Date().toLocaleString("en-IN")}
        </div>
        <table>
          <thead><tr><th>Date</th><th>ID</th><th>Type</th><th>Counterparty</th><th>Remarks</th><th>Amount</th><th>Balance</th></tr></thead>
          <tbody>${rows.map((tx) => `<tr><td>${formatDateTime(tx.createdAt)}</td><td>${tx.txId}</td><td>${normalizeType(tx.type)}</td><td>${tx.counterpartyName || "-"}</td><td>${tx.description || "-"}</td><td>${money(tx.amount)}</td><td>${tx.balanceAfterTransaction == null ? "-" : money(tx.balanceAfterTransaction)}</td></tr>`).join("")}</tbody>
        </table>
      </body>
    </html>
  `);
  printWindow.document.close();
  printWindow.print();
}

createRoot(document.getElementById("root")).render(<App />);
