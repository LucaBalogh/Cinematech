export const getLogger: (tag: string) => (args: any) => void = (tag) => (
  args
) => console.log(tag, args);

export const config = {
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*"
  },
};
