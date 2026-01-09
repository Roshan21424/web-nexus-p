import { twMerge } from "tailwind-merge";

export const Card = ({ className = "", children, ...props }) => (
  <div
    {...props}
    className={twMerge(
      "bg-white rounded-2xl border border-slate-200 p-6 shadow-sm hover:shadow-lg transition-all",
      className
    )}
  >
    {children}
  </div>
);
