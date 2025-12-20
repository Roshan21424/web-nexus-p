import React, { useState, useEffect } from "react";
import {
  Plus,
  Edit2,
  Trash2,
  Save,
  X,
  Users,
  BookOpen,
  School,
  User,
} from "lucide-react";
import api from "../service/api";

const API_BASE = process.env.REACT_APP_SERVER_URL;

export default function Admin() {
  const [activeTab, setActiveTab] = useState("sections");
  const [sections, setSections] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [students, setStudents] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState("");
  const [editingItem, setEditingItem] = useState(null);

  const [formData, setFormData] = useState({});

  const [assignmentData, setAssignmentData] = useState({
    teacherToSubject: { subjectId: "", teacherId: "" },
    sectionToSubject: { subjectId: "", sectionId: "" },
    classTeacherToSection: { teacherId: "", sectionId: "" },
    studentToSection: { studentId: "", sectionId: "" },
  });

  useEffect(() => {
    fetchAllData();
  }, []);

  const fetchAllData = async () => {
    setLoading(true);
    try {
      const [sectionsRes, teachersRes, studentsRes, subjectsRes] =
        await Promise.all([
          api.get(`${API_BASE}/admin/sections`).catch(() => null),
          api.get(`${API_BASE}/admin/teachers`).catch(() => null),
          api.get(`${API_BASE}/admin/students`).catch(() => null),
          api.get(`${API_BASE}/admin/subjects`).catch(() => null),
        ]);

      if (sectionsRes?.data) setSections(sectionsRes.data);
      if (teachersRes?.data) setTeachers(teachersRes.data);
      if (studentsRes?.data) setStudents(studentsRes.data);
      if (subjectsRes?.data) setSubjects(subjectsRes.data);

      setError(null);
    } catch (err) {
      setError("Failed to fetch data: " + err.message);
    }
    setLoading(false);
  };

  const handleCreate = async (type) => {
    try {
      let endpoint = "";
      let body = {};

      switch (type) {
        case "section":
          endpoint = "/admin/add-section";
          body = { sectionEnum: formData.sectionName };
          break;
        case "teacher":
          endpoint = "/admin/add-teacher";
          body = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
          };
          break;
        case "student":
          endpoint = "/admin/add-student";
          body = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
          };
          break;
        case "subject":
          endpoint = "/admin/add-subject";
          body = { name: formData.name };
          break;
      }

      await api.post(`${API_BASE}${endpoint}`, body);
      setShowModal(false);
      setFormData({});
      fetchAllData();
      alert("Created successfully!");
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleDelete = async (type, id) => {
    if (!window.confirm("Are you sure you want to delete this item?")) return;

    try {
      await api.delete(`${API_BASE}/admin/${type}/${id}`);
      alert("Deleted successfully!");
      fetchAllData();
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleAssignTeacherToSubject = async (subjectId, teacherId) => {
    try {
      await api.put(
        `${API_BASE}/admin/${subjectId}/assign-teacher/${teacherId}`
      );
      alert("Teacher assigned successfully!");
      fetchAllData();
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleAssignSectionToSubject = async (subjectId, sectionId) => {
    try {
      await api.put(
        `${API_BASE}/admin/${subjectId}/assign-section/${sectionId}`
      );
      alert("Section assigned successfully!");
      fetchAllData();
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleAssignClassTeacher = async (teacherId, sectionId) => {
    try {
      await api.put(
        `${API_BASE}/admin/${teacherId}/assign-class-teacher/${sectionId}`
      );
      alert("Class teacher assigned successfully!");
      fetchAllData();
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleAssignStudentToSection = async (studentId, sectionId) => {
    try {
      await api.put(
        `${API_BASE}/admin/${studentId}/assign-student/${sectionId}`
      );
      alert("Student assigned successfully!");
      fetchAllData();
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const handleQuickAssignment = async (type) => {
    try {
      switch (type) {
        case "teacherToSubject":
          const { subjectId: sId, teacherId: tId } =
            assignmentData.teacherToSubject;
          if (!sId || !tId) {
            alert("Please select both subject and teacher");
            return;
          }
          await handleAssignTeacherToSubject(sId, tId);
          setAssignmentData({
            ...assignmentData,
            teacherToSubject: { subjectId: "", teacherId: "" },
          });
          break;
        case "sectionToSubject":
          const { subjectId: subId, sectionId: secId } =
            assignmentData.sectionToSubject;
          if (!subId || !secId) {
            alert("Please select both subject and section");
            return;
          }
          await handleAssignSectionToSubject(subId, secId);
          setAssignmentData({
            ...assignmentData,
            sectionToSubject: { subjectId: "", sectionId: "" },
          });
          break;
        case "classTeacherToSection":
          const { teacherId: ctId, sectionId: ctSecId } =
            assignmentData.classTeacherToSection;
          if (!ctId || !ctSecId) {
            alert("Please select both teacher and section");
            return;
          }
          await handleAssignClassTeacher(ctId, ctSecId);
          setAssignmentData({
            ...assignmentData,
            classTeacherToSection: { teacherId: "", sectionId: "" },
          });
          break;
        case "studentToSection":
          const { studentId: stId, sectionId: stSecId } =
            assignmentData.studentToSection;
          if (!stId || !stSecId) {
            alert("Please select both student and section");
            return;
          }
          await handleAssignStudentToSection(stId, stSecId);
          setAssignmentData({
            ...assignmentData,
            studentToSection: { studentId: "", sectionId: "" },
          });
          break;
      }
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || err.message));
    }
  };

  const openModal = (type, item = null) => {
    setModalType(type);
    setEditingItem(item);
    setFormData(item || {});
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setFormData({});
    setEditingItem(null);
  };

  const handleDragStart = (e, item, type) => {
    e.dataTransfer.setData("item", JSON.stringify(item));
    e.dataTransfer.setData("type", type);
  };

  const handleDrop = (e, targetType, targetId) => {
    e.preventDefault();
    const item = JSON.parse(e.dataTransfer.getData("item"));
    const sourceType = e.dataTransfer.getData("type");

    if (sourceType === "teacher" && targetType === "subject") {
      handleAssignTeacherToSubject(targetId, item.id || item.teacherId);
    } else if (sourceType === "section" && targetType === "subject") {
      handleAssignSectionToSubject(targetId, item.id || item.sectionId);
    } else if (sourceType === "teacher" && targetType === "section") {
      handleAssignClassTeacher(item.id || item.teacherId, targetId);
    } else if (sourceType === "student" && targetType === "section") {
      handleAssignStudentToSection(item.id || item.studentId, targetId);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-gray-800">
          Nexus Admin Panel
        </h1>

        {loading && (
          <div className="mb-4 p-4 bg-blue-100 text-blue-700 rounded">
            Loading data...
          </div>
        )}

        <div className="flex gap-2 mb-6 border-b">
          <button
            onClick={() => setActiveTab("sections")}
            className={`px-4 py-2 font-medium ${
              activeTab === "sections"
                ? "border-b-2 border-blue-500 text-blue-600"
                : "text-gray-600"
            }`}
          >
            <School className="inline mr-2" size={18} />
            Sections
          </button>
          <button
            onClick={() => setActiveTab("teachers")}
            className={`px-4 py-2 font-medium ${
              activeTab === "teachers"
                ? "border-b-2 border-blue-500 text-blue-600"
                : "text-gray-600"
            }`}
          >
            <User className="inline mr-2" size={18} />
            Teachers
          </button>
          <button
            onClick={() => setActiveTab("students")}
            className={`px-4 py-2 font-medium ${
              activeTab === "students"
                ? "border-b-2 border-blue-500 text-blue-600"
                : "text-gray-600"
            }`}
          >
            <Users className="inline mr-2" size={18} />
            Students
          </button>
          <button
            onClick={() => setActiveTab("subjects")}
            className={`px-4 py-2 font-medium ${
              activeTab === "subjects"
                ? "border-b-2 border-blue-500 text-blue-600"
                : "text-gray-600"
            }`}
          >
            <BookOpen className="inline mr-2" size={18} />
            Subjects
          </button>
          <button
            onClick={() => setActiveTab("assignments")}
            className={`px-4 py-2 font-medium ${
              activeTab === "assignments"
                ? "border-b-2 border-blue-500 text-blue-600"
                : "text-gray-600"
            }`}
          >
            Assignments
          </button>
        </div>

        {activeTab === "sections" && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold">Sections</h2>
              <button
                onClick={() => openModal("section")}
                className="bg-blue-500 text-white px-4 py-2 rounded flex items-center gap-2 hover:bg-blue-600"
              >
                <Plus size={18} /> Add Section
              </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {sections.map((section) => (
                <div
                  key={section.id || section.sectionId}
                  className="bg-white p-4 rounded shadow border-2 border-dashed border-gray-300"
                  onDrop={(e) =>
                    handleDrop(e, "section", section.id || section.sectionId)
                  }
                  onDragOver={handleDragOver}
                >
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-bold text-lg">
                      {section.sectionName || section.name}
                    </h3>
                    <div className="flex gap-2">
                      <button
                        onClick={() => openModal("section", section)}
                        className="text-blue-500 hover:text-blue-700"
                      >
                        <Edit2 size={16} />
                      </button>
                      <button
                        onClick={() =>
                          handleDelete(
                            "sections",
                            section.id || section.sectionId
                          )
                        }
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                  <p className="text-sm text-gray-600">
                    Class Teacher: {section.classTeacherName || "Not assigned"}
                  </p>
                  <p className="text-sm text-gray-600">
                    Students: {section.studentCount || 0}
                  </p>
                  <p className="text-xs text-gray-400 mt-2">
                    Drop teachers/students here to assign
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "teachers" && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold">Teachers</h2>
              <button
                onClick={() => openModal("teacher")}
                className="bg-blue-500 text-white px-4 py-2 rounded flex items-center gap-2 hover:bg-blue-600"
              >
                <Plus size={18} /> Add Teacher
              </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {teachers.map((teacher) => (
                <div
                  key={teacher.id || teacher.teacherId}
                  draggable
                  onDragStart={(e) => handleDragStart(e, teacher, "teacher")}
                  className="bg-white p-4 rounded shadow cursor-move hover:shadow-lg transition"
                >
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <h3 className="font-bold">{teacher.name}</h3>
                      <p className="text-sm text-gray-600">{teacher.email}</p>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => openModal("teacher", teacher)}
                        className="text-blue-500 hover:text-blue-700"
                      >
                        <Edit2 size={16} />
                      </button>
                      <button
                        onClick={() =>
                          handleDelete(
                            "teachers",
                            teacher.id || teacher.teacherId
                          )
                        }
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                  <p className="text-xs text-gray-400">Drag to assign</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "students" && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold">Students</h2>
              <button
                onClick={() => openModal("student")}
                className="bg-blue-500 text-white px-4 py-2 rounded flex items-center gap-2 hover:bg-blue-600"
              >
                <Plus size={18} /> Add Student
              </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              {students.map((student) => (
                <div
                  key={student.id || student.studentId}
                  draggable
                  onDragStart={(e) => handleDragStart(e, student, "student")}
                  className="bg-white p-4 rounded shadow cursor-move hover:shadow-lg transition"
                >
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <h3 className="font-bold">{student.name}</h3>
                      <p className="text-sm text-gray-600">{student.email}</p>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => openModal("student", student)}
                        className="text-blue-500 hover:text-blue-700"
                      >
                        <Edit2 size={16} />
                      </button>
                      <button
                        onClick={() =>
                          handleDelete(
                            "students",
                            student.id || student.studentId
                          )
                        }
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                  <p className="text-xs text-gray-400">Drag to assign</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "subjects" && (
          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold">Subjects</h2>
              <button
                onClick={() => openModal("subject")}
                className="bg-blue-500 text-white px-4 py-2 rounded flex items-center gap-2 hover:bg-blue-600"
              >
                <Plus size={18} /> Add Subject
              </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {subjects.map((subject) => (
                <div
                  key={subject.id || subject.subjectId}
                  className="bg-white p-4 rounded shadow border-2 border-dashed border-gray-300"
                  onDrop={(e) =>
                    handleDrop(e, "subject", subject.id || subject.subjectId)
                  }
                  onDragOver={handleDragOver}
                >
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-bold text-lg">
                      {subject.name || subject.subjectName}
                    </h3>
                    <div className="flex gap-2">
                      <button
                        onClick={() => openModal("subject", subject)}
                        className="text-blue-500 hover:text-blue-700"
                      >
                        <Edit2 size={16} />
                      </button>
                      <button
                        onClick={() =>
                          handleDelete(
                            "subjects",
                            subject.id || subject.subjectId
                          )
                        }
                        className="text-red-500 hover:text-red-700"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                  <p className="text-sm text-gray-600">
                    Teacher: {subject.teacherName || "Not assigned"}
                  </p>
                  <p className="text-sm text-gray-600">
                    Sections: {subject.sectionCount || 0}
                  </p>
                  <p className="text-xs text-gray-400 mt-2">
                    Drop teachers/sections here to assign
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "assignments" && (
          <div className="bg-white p-6 rounded shadow">
            <h2 className="text-2xl font-bold mb-4">Quick Assignments</h2>
            <div className="space-y-6">
              <div>
                <h3 className="font-bold mb-2">Assign Teacher to Subject</h3>
                <div className="flex gap-2">
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.teacherToSubject.subjectId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        teacherToSubject: {
                          ...assignmentData.teacherToSubject,
                          subjectId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Subject</option>
                    {subjects.map((s) => (
                      <option
                        key={s.id || s.subjectId}
                        value={s.id || s.subjectId}
                      >
                        {s.name || s.subjectName}
                      </option>
                    ))}
                  </select>
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.teacherToSubject.teacherId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        teacherToSubject: {
                          ...assignmentData.teacherToSubject,
                          teacherId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Teacher</option>
                    {teachers.map((t) => (
                      <option
                        key={t.id || t.teacherId}
                        value={t.id || t.teacherId}
                      >
                        {t.name}
                      </option>
                    ))}
                  </select>
                  <button
                    onClick={() => handleQuickAssignment("teacherToSubject")}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Assign
                  </button>
                </div>
              </div>

              <div>
                <h3 className="font-bold mb-2">Assign Section to Subject</h3>
                <div className="flex gap-2">
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.sectionToSubject.subjectId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        sectionToSubject: {
                          ...assignmentData.sectionToSubject,
                          subjectId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Subject</option>
                    {subjects.map((s) => (
                      <option
                        key={s.id || s.subjectId}
                        value={s.id || s.subjectId}
                      >
                        {s.name || s.subjectName}
                      </option>
                    ))}
                  </select>
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.sectionToSubject.sectionId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        sectionToSubject: {
                          ...assignmentData.sectionToSubject,
                          sectionId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Section</option>
                    {sections.map((s) => (
                      <option
                        key={s.id || s.sectionId}
                        value={s.id || s.sectionId}
                      >
                        {s.sectionName || s.name}
                      </option>
                    ))}
                  </select>
                  <button
                    onClick={() => handleQuickAssignment("sectionToSubject")}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Assign
                  </button>
                </div>
              </div>

              <div>
                <h3 className="font-bold mb-2">
                  Assign Class Teacher to Section
                </h3>
                <div className="flex gap-2">
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.classTeacherToSection.teacherId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        classTeacherToSection: {
                          ...assignmentData.classTeacherToSection,
                          teacherId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Teacher</option>
                    {teachers.map((t) => (
                      <option
                        key={t.id || t.teacherId}
                        value={t.id || t.teacherId}
                      >
                        {t.name}
                      </option>
                    ))}
                  </select>
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.classTeacherToSection.sectionId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        classTeacherToSection: {
                          ...assignmentData.classTeacherToSection,
                          sectionId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Section</option>
                    {sections.map((s) => (
                      <option
                        key={s.id || s.sectionId}
                        value={s.id || s.sectionId}
                      >
                        {s.sectionName || s.name}
                      </option>
                    ))}
                  </select>
                  <button
                    onClick={() =>
                      handleQuickAssignment("classTeacherToSection")
                    }
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Assign
                  </button>
                </div>
              </div>

              <div>
                <h3 className="font-bold mb-2">Assign Student to Section</h3>
                <div className="flex gap-2">
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.studentToSection.studentId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        studentToSection: {
                          ...assignmentData.studentToSection,
                          studentId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Student</option>
                    {students.map((s) => (
                      <option
                        key={s.id || s.studentId}
                        value={s.id || s.studentId}
                      >
                        {s.name}
                      </option>
                    ))}
                  </select>
                  <select
                    className="border p-2 rounded flex-1"
                    value={assignmentData.studentToSection.sectionId}
                    onChange={(e) =>
                      setAssignmentData({
                        ...assignmentData,
                        studentToSection: {
                          ...assignmentData.studentToSection,
                          sectionId: e.target.value,
                        },
                      })
                    }
                  >
                    <option value="">Select Section</option>
                    {sections.map((s) => (
                      <option
                        key={s.id || s.sectionId}
                        value={s.id || s.sectionId}
                      >
                        {s.sectionName || s.name}
                      </option>
                    ))}
                  </select>
                  <button
                    onClick={() => handleQuickAssignment("studentToSection")}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Assign
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {showModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded shadow-lg w-96">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold">
                  {editingItem ? "Edit" : "Add"} {modalType}
                </h3>
                <button
                  onClick={closeModal}
                  className="text-gray-500 hover:text-gray-700"
                >
                  <X size={24} />
                </button>
              </div>

              <div className="space-y-4">
                {modalType === "section" && (
                  <input
                    type="text"
                    placeholder="Section Name (e.g., SECTION_A)"
                    className="w-full border p-2 rounded"
                    value={formData.sectionName || ""}
                    onChange={(e) =>
                      setFormData({ ...formData, sectionName: e.target.value })
                    }
                  />
                )}

                {(modalType === "teacher" || modalType === "student") && (
                  <>
                    <input
                      type="text"
                      placeholder="Name"
                      className="w-full border p-2 rounded"
                      value={formData.name || ""}
                      onChange={(e) =>
                        setFormData({ ...formData, name: e.target.value })
                      }
                    />
                    <input
                      type="email"
                      placeholder="Email"
                      className="w-full border p-2 rounded"
                      value={formData.email || ""}
                      onChange={(e) =>
                        setFormData({ ...formData, email: e.target.value })
                      }
                    />
                    <input
                      type="password"
                      placeholder="Password"
                      className="w-full border p-2 rounded"
                      value={formData.password || ""}
                      onChange={(e) =>
                        setFormData({ ...formData, password: e.target.value })
                      }
                    />
                  </>
                )}

                {modalType === "subject" && (
                  <input
                    type="text"
                    placeholder="Subject Name"
                    className="w-full border p-2 rounded"
                    value={formData.name || ""}
                    onChange={(e) =>
                      setFormData({ ...formData, name: e.target.value })
                    }
                  />
                )}
              </div>

              <div className="flex gap-2 mt-6">
                <button
                  onClick={() => handleCreate(modalType)}
                  className="flex-1 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center justify-center gap-2"
                >
                  <Save size={18} />
                  {editingItem ? "Update" : "Create"}
                </button>
                <button
                  onClick={closeModal}
                  className="flex-1 bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400"
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}

        {error && (
          <div className="fixed bottom-4 right-4 bg-red-500 text-white p-4 rounded shadow-lg">
            {error}
          </div>
        )}
      </div>
    </div>
  );
}
