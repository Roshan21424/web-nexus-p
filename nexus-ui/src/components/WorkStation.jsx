import { useState, useEffect } from "react";
import { Spinner } from "./ui/Spinner";
import { Avatar } from "./ui/Avatar";
import { Card } from "./ui/Card";
import { useMyContext } from "../context/ContextProvider";

const getStatusBadge = (status) => {
  const map = {
    PENDING: "bg-amber-100 text-amber-700",
    CONFORMATION: "bg-blue-100 text-blue-700",
    COMPLETED: "bg-emerald-100 text-emerald-700",
  };

  const label = {
    PENDING: "Pending",
    CONFORMATION: "Needs Confirmation",
    COMPLETED: "Completed",
  };

  return (
    <span
      className={`px-3 py-1 rounded-full text-xs font-semibold ${map[status]}`}
    >
      {label[status]}
    </span>
  );
};

const extract = (items, type) => {
  const map = {};

  items.forEach((item) => {
    item.assigner.forEach((as) => {
      const entries = type === "task" ? as.context : as.value;

      entries.forEach((entry) => {
        const msg = type === "task" ? entry.value : entry;

        if (!map[msg]) {
          map[msg] = {
            message: msg,
            status: type === "task" ? entry.status : null,
            by: as.by,
            to: [item.to],
          };
        } else {
          if (!map[msg].to.includes(item.to)) map[msg].to.push(item.to);
        }
      });
    });
  });

  return Object.values(map);
};

export default function WorkStation() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const { setCurrentRoute } = useMyContext();

  useEffect(() => {
    setCurrentRoute("WorkStation");
  }, []);

  useEffect(() => {
    setTimeout(() => {
      setData({
        today_remainder: [
          {
            to: "Class 10-A",
            assigner: [
              { by: "Mr. Patel", value: ["Submit assignment by 5 PM"] },
            ],
          },
        ],
        today_task: [
          {
            to: "Class 10-A",
            assigner: [
              {
                by: "Ms. Gupta",
                context: [
                  { value: "Complete chapter 5 reading", status: "PENDING" },
                ],
              },
            ],
          },
        ],
      });
      setLoading(false);
    }, 500);
  }, []);

  if (loading) return <Spinner text="Loading workstation..." />;

  const reminders = extract(data.today_remainder, "rem");
  const tasks = extract(data.today_task, "task");

  return (
    <div className="min-h-screen p-6 bg-gradient-to-br from-slate-50 to-slate-100">
      <div className="max-w-6xl mx-auto">
        {" "}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <div>
            <h2 className="text-3xl font-bold text-slate-900">
              Today's Reminders
            </h2>
            <p className="text-slate-500 text-sm mb-4">
              Stay aware of today's notices
            </p>

            {reminders.length === 0 ? (
              <Card className="text-center">No reminders.</Card>
            ) : (
              reminders.map((r) => (
                <Card key={r.message} className="hover:border-purple-300">
                  <div className="flex items-center gap-4 mb-4">
                    <Avatar name={r.by} />
                    <div>
                      <p className="text-xs text-slate-500">From</p>
                      <p className="font-semibold text-slate-900">{r.by}</p>
                    </div>
                  </div>

                  <p className="text-slate-700 font-medium">{r.message}</p>
                  <p className="text-xs text-slate-500 mt-2">
                    To: {r.to.join(", ")}
                  </p>
                </Card>
              ))
            )}
          </div>

          <div>
            <h2 className="text-3xl font-bold text-slate-900">Today's Tasks</h2>
            <p className="text-slate-500 text-sm mb-4">Your daily task list</p>

            {tasks.length === 0 ? (
              <Card className="text-center">No tasks.</Card>
            ) : (
              tasks.map((t) => (
                <Card key={t.message} className="hover:border-purple-300">
                  <div className="flex items-center gap-4 mb-4">
                    <Avatar name={t.by} />
                    <div>
                      <p className="text-xs text-slate-500">From</p>
                      <p className="font-semibold text-slate-900">{t.by}</p>
                    </div>
                  </div>

                  <p className="text-slate-700 font-medium mb-3">{t.message}</p>

                  <div className="flex justify-between items-center">
                    <p className="text-xs text-slate-500">
                      To: {t.to.join(", ")}
                    </p>
                    {getStatusBadge(t.status)}
                  </div>
                </Card>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
