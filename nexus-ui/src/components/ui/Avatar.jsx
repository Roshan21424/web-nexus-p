export const Avatar = ({ name, size = "md" }) => {
  const initials = name
    ?.split(" ")
    .map((n) => n[0])
    .join("")
    .toUpperCase();

  const sizes = {
    sm: "w-8 h-8 text-xs",
    md: "w-10 h-10 text-sm",
    lg: "w-14 h-14 text-lg",
  };

  return (
    <div
      className={`flex items-center justify-center rounded-full text-white font-bold bg-gradient-to-br from-purple-500 to-blue-500 ${sizes[size]}`}
    >
      {initials}
    </div>
  );
};
