import os
import random
import string

file_name = "sample_file_name.txt"

if __name__ == '__main__':
    if os.path.isfile(file_name):
        os.remove(file_name)

    fs = open(file_name, 'w')

    for emp_year in ['90', '92', '95', '01', '04', '05', '08', '12', '15', '19']:
        for emp_num in range(0, 10000):
            first_name = ''.join(random.choices(string.ascii_uppercase,k=6))
            last_name = random.choice(['KIM','LEE','PARK', 'HAN', 'HWANG', 'CHOI', 'JANG'])
            mid_phonenum = ''.join(random.choices(string.digits, k=4))
            last_phonenum = ''.join(random.choices(string.digits, k=4))
            birth_year = str(random.randint(1975, 2002))
            birth_month = '{0:0>2}'.format(random.randint(1, 12))
            birth_day = '{0:0>2}'.format(random.randint(1, 28))

            fs.writelines('ADD, , , ,' + emp_year + '{0:0>6}'.format(emp_num) + ',' + first_name + ' ' + last_name + ',' +
                          random.choice(['CL1','CL2','CL3','CL4']) + ',010-' + mid_phonenum + '-' + last_phonenum + ',' +
                          birth_year + birth_month + birth_day + ',' + random.choice(['ADV','PRO','EX']) + '\n')

    fs.close()
