import { twMerge } from "tailwind-merge";

export const Button = ({ variant = "primary", size = "md", className = "", children, ...props }) => {
  const base =
    "inline-flex items-center justify-center gap-2 font-semibold rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed";

  const variants = {
    primary:
      "text-white bg-gradient-to-r from-purple-500 to-blue-500 hover:from-purple-600 hover:to-blue-600 hover:shadow-lg",
    outline:
      "border border-slate-300 text-slate-700 hover:bg-slate-100",
    ghost: "text-slate-600 hover:bg-slate-100",
  };

  const sizes = {
    sm: "px-3 py-1.5 text-sm",
    md: "px-4 py-2",
    lg: "px-5 py-3 text-lg",
    full: "w-full py-3",
  };

  return (
    <button
      {...props}
      className={twMerge(base, variants[variant], sizes[size], className)}
    >
      {children}
    </button>
  );
};
