export const UploadBox = ({ file, label, onChange }) => {
  return (
    <label className="cursor-pointer block">
      <input type="file" className="hidden" onChange={onChange} accept="image/*" />

      <div
        className={`border-2 border-dashed rounded-xl px-6 py-8 text-center transition-all bg-slate-50 hover:bg-slate-100 
        ${file ? "border-purple-500" : "border-slate-300"}`}
      >
        <p className="text-sm font-semibold text-slate-900">
          {file ? file.name : label}
        </p>
        <p className="text-xs text-slate-500 mt-1">PNG, JPG up to 10MB</p>
      </div>
    </label>
  );
};
