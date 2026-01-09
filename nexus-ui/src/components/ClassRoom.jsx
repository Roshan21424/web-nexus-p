"use client";

import { useState, useEffect } from "react";
import api from "../service/api";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";
import { UploadBox } from "../components/ui/UploadBox";
import { Spinner } from "../components/ui/Spinner";
import { Avatar } from "../components/ui/Avatar";

const BRANCH_MAP = {
  CS: "Computer Science",
  IT: "Information Technology",
  MECH: "Mechanical Engineering",
  CIVIL: "Civil Engineering",
  AIML: "AI & Machine Learning",
  ECE: "Electronics & Communication",
  EEE: "Electrical & Electronics",
};

const expandSectionName = (name) => {
  const [branch, year, section] = name.split("_");
  return branch && year && section
    ? `${BRANCH_MAP[branch] || branch} (Year ${year}, Section ${section})`
    : name;
};

export default function ClassRoom() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const [timetableFile, setTimetableFile] = useState(null);
  const [semesterFile, setSemesterFile] = useState(null);
  const [uploading, setUploading] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get("/class/get-section?sectionId=1");
        setData(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleUpload = (type) => {
    const file = type === "timetable" ? timetableFile : semesterFile;
    if (!file) return alert("Please select a file first.");

    setUploading(true);
    setTimeout(() => {
      alert("File uploaded successfully!");
      setUploading(false);
      if (type === "timetable") setTimetableFile(null);
      else setSemesterFile(null);
    }, 1000);
  };

  if (loading) return <Spinner text="Loading section details..." />;
  if (!data) return null;

  // Events parsing
  let events = [];
  try {
    events = JSON.parse(data.events)?.events || [];
  } catch {}

  const classReps = data.studentDTOList.filter(
    (s) => s.role === "CR" || s.role === "GR"
  );
  const regularStudents = data.studentDTOList.filter(
    (s) => !s.role || s.role === "NORMAL"
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 text-slate-900">
      <div className="max-w-6xl mx-auto px-6 py-8 space-y-10">

        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <h1 className="text-3xl font-bold">
            {expandSectionName(data.sectionName)}
          </h1>

          <Card className="px-6 py-3 flex items-center gap-3 shadow-sm border border-slate-200">
            <span className="text-2xl font-bold text-purple-600">
              {data.studentDTOList.length}
            </span>
            <span className="text-sm font-semibold text-slate-600">
              Students
            </span>
          </Card>
        </div>

        <Card className="bg-gradient-to-r from-purple-500 to-blue-500 text-white flex items-center gap-4 py-5 px-6 shadow-lg border-none">
          <Avatar name={data.classTeacherDTO.classTeacherName} size="md" />

          <div>
            <p className="text-sm opacity-80">Class Teacher</p>
            <h3 className="text-xl font-bold">
              {data.classTeacherDTO.classTeacherName}
            </h3>
          </div>
        </Card>

        <section>
          <h2 className="text-2xl font-bold mb-4">Subjects</h2>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {data.subjectDTOList.map((sub) => (
              <Card
                key={sub.subjectID}
                className="hover:border-purple-300 transition-all"
              >
                <p className="text-xs font-semibold text-slate-500 mb-1">
                  {sub.subjectTeacherName}
                </p>
                <p className="font-bold">{sub.subjectName}</p>
              </Card>
            ))}
          </div>
        </section>

        <section>
          <h2 className="text-2xl font-bold mb-4">Students</h2>

          {classReps.length > 0 && (
            <>
              <h3 className="text-sm font-bold uppercase mb-3 pl-3 border-l-4 border-purple-500">
                Class Heads (CR / GR)
              </h3>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3 mb-6">
                {classReps.map((s) => (
                  <Card
                    key={s.studentId}
                    className="flex items-center gap-3 bg-gradient-to-r from-purple-100 to-blue-100 border-purple-200"
                  >
                    <Avatar name={s.name} size="sm" />

                    <div className="flex-1">
                      <p className="font-semibold">{s.name}</p>
                      <p className="text-xs text-slate-600">#{s.studentId}</p>
                    </div>

                    <span className="bg-white text-purple-600 px-2 py-1 rounded text-xs font-bold">
                      {s.role}
                    </span>
                  </Card>
                ))}
              </div>
            </>
          )}

          <h3 className="text-sm font-bold uppercase mb-3 pl-3 border-l-4 border-blue-500">
            Regular Students ({regularStudents.length})
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
            {regularStudents.map((s) => (
              <Card key={s.studentId} className="flex items-center gap-3">
                <Avatar name={s.name} size="sm" />

                <div>
                  <p className="text-xs text-slate-500">Roll No: {s.studentId}</p>
                  <p className="font-semibold">{s.name}</p>
                </div>
              </Card>
            ))}
          </div>
        </section>

        {events.length > 0 && (
      <section>
  <h2 className="text-2xl font-bold mb-4">Events</h2>

  <div className="space-y-4">
    {events.map((ev) => (
      <Card key={ev.id} className="hover:border-purple-300">
        <div className="flex justify-between items-start gap-4">
          
          <div className="flex-1">
            <p className="text-sm text-purple-600">
              From: {ev.teacherName}
            </p>
            <h3 className="font-bold text-lg">{ev.title}</h3>
            <p className="text-slate-600">{ev.content}</p>
          </div>

          <span className="flex-shrink-0 inline-block px-3 py-1 text-xs rounded bg-purple-600 text-white">
            {ev.date}
          </span>

        </div>
      </Card>
    ))}
  </div>
</section>

        )}

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">

          <section>
            <h2 className="text-xl font-bold mb-4">Timetable</h2>

            {data.timetableImageBase64 && (
              <Card className="p-0 overflow-hidden max-h-64">
                <img
                  src={data.timetableImageBase64}
                  className="w-full object-contain"
                />
              </Card>
            )}

            <div className="flex gap-2 mt-4">
              <UploadBox
                file={timetableFile}
                label="Choose timetable file"
                onChange={(e) => setTimetableFile(e.target.files?.[0] || null)}
              />

              <Button
                disabled={!timetableFile || uploading}
                onClick={() => handleUpload("timetable")}
              >
                {uploading ? "Uploading..." : "Upload"}
              </Button>
            </div>
          </section>

          <section>
            <h2 className="text-xl font-bold mb-4">Schedule</h2>

            {data.semesterScheduleImageBase64 && (
              <Card className="p-0 overflow-hidden max-h-64">
                <img
                  src={data.semesterScheduleImageBase64}
                  className="w-full object-contain"
                />
              </Card>
            )}

            <div className="flex gap-2 mt-4">
              <UploadBox
                file={semesterFile}
                label="Choose schedule file"
                onChange={(e) => setSemesterFile(e.target.files?.[0] || null)}
              />

              <Button
                disabled={!semesterFile || uploading}
                onClick={() => handleUpload("semester")}
                variant="primary"
              >
                {uploading ? "Uploading..." : "Upload"}
              </Button>
            </div>
          </section>

        </div>
      </div>
    </div>
  );
}
