export const Spinner = ({ text }) => (
  <div className="flex items-center justify-center h-screen bg-gradient-to-br from-slate-50 to-slate-100">
    <div className="text-center">
      <div className="w-12 h-12 border-4 border-purple-200 border-t-purple-600 rounded-full animate-spin mx-auto mb-4" />
      {text && <p className="text-slate-600 font-medium">{text}</p>}
    </div>
  </div>
);
