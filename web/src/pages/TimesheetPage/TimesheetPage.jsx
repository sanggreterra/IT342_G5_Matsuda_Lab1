import { useState } from 'react';
import './TimesheetPage.css';

export default function TimesheetPage() {
  const [employees, setEmployees] = useState([]);
  const [records, setRecords] = useState([]);
  const [activeRecords, setActiveRecords] = useState({});
  const [summary, setSummary] = useState({});
  const [selectedEmployee, setSelectedEmployee] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [newEmployeeName, setNewEmployeeName] = useState('');

  const isClockOutEnabled = selectedEmployee && activeRecords[selectedEmployee];

  const handleAddEmployee = (e) => {
    e.preventDefault();
    const name = newEmployeeName.trim();

    if (!name) {
      alert('Employee name cannot be empty!');
      return;
    }

    if (employees.includes(name)) {
      alert('Employee already exists!');
      return;
    }

    setEmployees((prev) => [...prev, name]);
    setNewEmployeeName('');
  };

  const handleEmployeeSelect = (e) => {
    setSelectedEmployee(e.target.value);
  };

  const handleClockIn = () => {
    if (!selectedEmployee) {
      alert('Please select an employee!');
      return;
    }

    if (activeRecords[selectedEmployee]) {
      alert(`${selectedEmployee} has already clocked in!`);
      return;
    }

    const clockInTime = new Date();
    setActiveRecords((prev) => ({
      ...prev,
      [selectedEmployee]: { employee: selectedEmployee, clockIn: clockInTime },
    }));
  };

  const formatDateTime = (date) => {
    return `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()} ${date.toLocaleTimeString()}`;
  };

  const handleClockOut = () => {
    if (!selectedEmployee || !activeRecords[selectedEmployee]) {
      alert('Please clock in first!');
      return;
    }

    const clockOutTime = new Date();
    const clockInTime = activeRecords[selectedEmployee].clockIn;
    const hoursWorked = ((clockOutTime - clockInTime) / (1000 * 60 * 60)).toFixed(2);

    const formattedClockIn = formatDateTime(clockInTime);
    const formattedClockOut = formatDateTime(clockOutTime);

    setRecords((prev) => [
      ...prev,
      {
        employee: selectedEmployee,
        clockIn: formattedClockIn,
        clockOut: formattedClockOut,
        hoursWorked,
      },
    ]);

    setSummary((prev) => {
      const current = prev[selectedEmployee] || 0;
      return { ...prev, [selectedEmployee]: current + parseFloat(hoursWorked) };
    });

    setActiveRecords((prev) => {
      const next = { ...prev };
      delete next[selectedEmployee];
      return next;
    });
  };

  const filteredRecords = records.filter((record) =>
    record.employee.toLowerCase().includes(searchValue.toLowerCase())
  );

  return (
    <div className="container">
      <section className="column employee-management">
        <section className="employee-form">
          <h2>Add Employee</h2>
          <form onSubmit={handleAddEmployee}>
            <input
              type="text"
              value={newEmployeeName}
              onChange={(e) => setNewEmployeeName(e.target.value)}
              placeholder="Enter Employee Name"
              required
            />
            <button type="submit">Add Employee</button>
          </form>
        </section>

        <section className="timesheet-form">
          <h2>Clock In / Clock Out</h2>
          <form onSubmit={(e) => e.preventDefault()}>
            <label htmlFor="employee">Select Employee:</label>
            <select
              id="employee"
              value={selectedEmployee}
              onChange={handleEmployeeSelect}
              required
            >
              <option value="" disabled>
                Select Employee
              </option>
              {employees.map((emp) => (
                <option key={emp} value={emp}>
                  {emp}
                </option>
              ))}
            </select>
            <button type="button" onClick={handleClockIn}>
              Clock In
            </button>
            <button
              type="button"
              onClick={handleClockOut}
              disabled={!isClockOutEnabled}
            >
              Clock Out
            </button>
          </form>
        </section>
      </section>

      <section className="column records">
        <h2>Timesheet Records</h2>
        <input
          type="text"
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          placeholder="Search Employee"
        />
        <table>
          <thead>
            <tr>
              <th>Employee</th>
              <th>Clock In</th>
              <th>Clock Out</th>
              <th>Hours Worked</th>
            </tr>
          </thead>
          <tbody>
            {filteredRecords.map((record, idx) => (
              <tr key={`${record.employee}-${record.clockIn}-${idx}`}>
                <td>{record.employee}</td>
                <td>{record.clockIn}</td>
                <td>{record.clockOut}</td>
                <td>{record.hoursWorked} hrs</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      <section className="column summary">
        <h2>Employee Summary</h2>
        <div className="summaryTable">
          {Object.keys(summary).map((employee) => (
            <p key={employee}>
              <strong>{employee}:</strong> {summary[employee].toFixed(2)} hrs
            </p>
          ))}
        </div>
      </section>
    </div>
  );
}
