import { useState, useEffect } from "react";
import api from "../service/api";

import { Button } from "./ui/Button";
import { UploadBox } from "./ui/UploadBox";
import { Spinner } from "./ui/Spinner";
import { Avatar } from "./ui/Avatar";

import {
  Plus,
  Upload,
  MoreHorizontal,
  Heart,
} from "lucide-react";

import { useMyContext } from "../context/ContextProvider";

export default function PostFeed() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [caption, setCaption] = useState("");
  const [likedPosts, setLikedPosts] = useState([]);

  const { setCurrentRoute } = useMyContext();

  useEffect(() => {
    setCurrentRoute("Feed");
  }, []);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get("/posts/get-all-post");
        setPosts(res.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile) return;

    const form = new FormData();
    form.append("file", selectedFile);

    try {
      setUploading(true);
      const res = await api.post("posts/upload-post", form);
      setPosts((prev) => [res.data, ...prev]);
      setSelectedFile(null);
      setCaption("");
      setShowCreateForm(false);
    } catch (e) {
      console.error(e);
    } finally {
      setUploading(false);
    }
  };

  const toggleLike = (id) => {
    setLikedPosts((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  if (loading) return <Spinner text="Loading posts..." />;

  return (
    <div className="min-h-screen p-6 bg-gradient-to-br from-slate-50 to-slate-100">
      <div className="max-w-6xl mx-auto">
        <div className="flex items-center justify-between mb-10">
          <div>
            <h1 className="text-4xl font-bold text-slate-900">Connect</h1>
            <p className="text-slate-500 text-sm mt-1">Share your moments</p>
          </div>

          <Button onClick={() => setShowCreateForm((p) => !p)} size="md">
            <Plus className="w-4 h-4" />
            {showCreateForm ? "Cancel" : "Create Post"}
          </Button>
        </div>

        {showCreateForm && (
          <div className="bg-white p-8 rounded-2xl shadow-lg border border-slate-200 mb-10">
            <form className="space-y-6" onSubmit={handleUpload}>
              <UploadBox
                file={selectedFile}
                label="Select an image"
                onChange={(e) => setSelectedFile(e.target.files?.[0] || null)}
              />

              <textarea
                className="w-full p-3 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-purple-500"
                rows={4}
                placeholder="Write a caption..."
                value={caption}
                onChange={(e) => setCaption(e.target.value)}
              />

              <Button size="full" disabled={!selectedFile || uploading}>
                {uploading ? (
                  <>
                    <Upload className="animate-pulse" />
                    Uploading...
                  </>
                ) : (
                  <>
                    <Upload />
                    Post
                  </>
                )}
              </Button>
            </form>
          </div>
        )}

        {posts.length === 0 ? (
          <div className="bg-white p-10 rounded-2xl border text-center text-slate-500">
            No posts yet.
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {posts.map((post) => {
              const liked = likedPosts.includes(post.id);

              return (
                <div
                  key={post.id}
                  className="bg-white rounded-2xl border border-slate-200 hover:border-purple-300 shadow-sm hover:shadow-xl transition-all overflow-hidden"
                >
                  <div className="flex items-center justify-between p-4 border-b border-slate-200">
                    <div className="flex items-center gap-3">
                      <Avatar name={post.username} size="md" />
                      <div>
                        <p className="font-semibold text-sm text-slate-900">
                          {post.username}
                        </p>
                        {post.role === "student" && (
                          <p className="text-xs text-slate-500">
                            {post.section}
                          </p>
                        )}
                      </div>
                    </div>

                    <button className="p-2 rounded-full hover:bg-slate-100 text-slate-500">
                      <MoreHorizontal />
                    </button>
                  </div>

                  <div className="h-56 bg-slate-100 overflow-hidden">
                    <img
                      src={`data:image/jpeg;base64,${post.imageData}`}
                      className="w-full h-full object-cover"
                    />
                  </div>

                  <div className="flex items-center justify-between p-4 border-t border-slate-200">
                    <span className="text-sm font-semibold text-slate-600">
                      {post.likes ?? 0} likes
                    </span>

                    <button
                      className={`flex items-center gap-1 text-sm font-semibold transition ${
                        liked ? "text-purple-600" : "text-slate-400"
                      }`}
                      onClick={() => toggleLike(post.id)}
                    >
                      <Heart
                        size={18}
                        fill={liked ? "currentColor" : "none"}
                        stroke="currentColor"
                      />
                      Like
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
