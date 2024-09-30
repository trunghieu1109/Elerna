import json

# Đường dẫn đến file văn bản chứa chuỗi JSON
file_path = "sim_map.txt"

# Đọc chuỗi JSON từ file văn bản
with open(file_path, "r") as file:
    json_string = file.read()
    
is_print = False
    
for c in json_string:
    
    if is_print:
        print(c)
        is_print = False
    
    if not (c == '\n'):
        print(c)

print(len(json_string))

# Chuyển đổi chuỗi thành đối tượng JSON
# json_data = json.loads(json_string)

# In ra dữ liệu JSON đã chuyển đổi
# print(json_data['lane']['id']['id'])