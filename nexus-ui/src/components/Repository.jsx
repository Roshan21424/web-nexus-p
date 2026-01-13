import { useState, useEffect } from "react";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";
import { Avatar } from "../components/ui/Avatar";
import { Spinner } from "../components/ui/Spinner";

import {
  FileText,
  Image,
  Archive,
  Paperclip,
  Download,
  Share2,
  Calendar,
  Star,
} from "lucide-react";

import { useMyContext } from "../context/ContextProvider";
import api from "../service/api";

export default function Repository() {
  const [files, setFiles] = useState([]);
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);

  const { setCurrentRoute ,sectionId} = useMyContext();

  useEffect(() => {
    setCurrentRoute("Database");
  }, [setCurrentRoute]);

  const fileIcons = {
    pdf: FileText,
    png: Image,
    jpg: Image,
    jpeg: Image,
    zip: Archive,
  };

  const getFileIcon = (type) => fileIcons[type] || Paperclip;


    useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get(`/class-repository/get-class-repository?sectionId=${sectionId}`);
        setFiles(res.data);
        console.log(res.data)
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const toggleFavorite = (id) => {
    setFavorites((prev) =>
      prev.includes(id)
        ? prev.filter((x) => x !== id)
        : [...prev, id]
    );
  };

  const openFile = (url) => {
    window.open(url, "_blank");
  };

  if (loading) {
    return <Spinner text="Loading repository..." />;
  }

  return (
    <div className="min-h-screen p-6 bg-gradient-to-br from-slate-50 to-slate-100">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold text-slate-900 mb-3">
          Class Repository
        </h1>
        <p className="text-slate-500 text-sm mb-10">
          Access and organize your class materials easily
        </p>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {files.map((file) => {
            const Icon = getFileIcon(file.type);
            const isFavorite = favorites.includes(file.id);

            return (
              <Card
                key={file.id}
                className="group flex flex-col hover:border-purple-300 transition"
              >
                <div className="flex justify-between items-start mb-4">
                  <button
                    className="flex items-center gap-3 flex-1 text-left"
                    onClick={() => openFile(file.url)}
                  >
                    <div className="p-2 rounded-xl bg-purple-100 group-hover:bg-purple-200 transition">
                      <Icon className="w-6 h-6 text-purple-600" />
                    </div>

                    <div className="min-w-0">
                      <h3 className="font-semibold text-slate-900 truncate">
                        {file.name}
                      </h3>
                      <p className="text-xs text-slate-500">{file.size}</p>
                    </div>
                  </button>

                  <button
                    onClick={() => toggleFavorite(file.id)}
                    className={`p-2 rounded-full hover:bg-slate-100 transition ${
                      isFavorite ? "text-purple-600" : "text-slate-300"
                    }`}
                  >
                    <Star
                      className="w-5 h-5"
                      fill={isFavorite ? "currentColor" : "none"}
                    />
                  </button>
                </div>

                <div className="text-xs space-y-2 mb-4 border-b pb-4 border-slate-200">
                  <div className="flex items-center gap-2 text-slate-600">
                    <span className="font-medium">{file.category}</span>
                    <span className="text-slate-300">•</span>
                    <span>{file.subject}</span>
                  </div>

                  <div className="flex items-center gap-1 text-slate-600">
                    <Calendar className="w-4 h-4" />
                    {file.uploadDate.toDateString()}
                  </div>
                </div>

                <div className="flex justify-between items-center text-xs mb-4 pb-4 border-b border-slate-200">
                  <div className="flex items-center gap-2">
                    <Avatar name={file.uploadedBy} size="sm" />
                    <span className="text-slate-600">
                      {file.uploadedBy}
                    </span>
                  </div>

                  <div className="flex items-center gap-1 text-slate-500">
                    <Download className="w-4 h-4" />
                    {file.downloads}
                  </div>
                </div>

                <div className="flex gap-2 mt-auto">
                  <Button
                    className="flex-1"
                    onClick={() => openFile(file.url)}
                  >
                    <Download />
                    Download
                  </Button>

                  <Button variant="outline" className="flex-1">
                    <Share2 />
                    Share
                  </Button>
                </div>
              </Card>
            );
          })}
        </div>
      </div>
    </div>
  );
}
