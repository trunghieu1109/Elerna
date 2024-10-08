import socket
import time

import cv2
from flask import Flask, request
import os
from datetime import timedelta
import json
import lgsvl
import numpy as np
import pickle
from ScenarioCollector.createUtils import *
from collision_utils import pedestrian, npc_vehicle, calculate_measures
import math
import threading
from lgsvl.agent import NpcVehicle
import random
import queue
import pandas as pd

import torch
from lgsvl.dreamview import CoordType
import requests
from numba import jit


########################################
observation_time = 6  # OTP [4, 6, 8, 10]

app = Flask(__name__)

app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=7)

print('create simulator')

sim = lgsvl.Simulator(os.environ.get(
    "SIMUSaveStateLATOR_HOST", "localhost"), 8977)

print('connected')
# state = lgsvl.AgentState()
# print(state.transform)
collision_object = None
probability = 0
time_step_collision_object = None
sensors = None
DATETIME_UNIX = None
WEATHER_DATA = None
TIMESTAMP = None
DESTINATION = None
EGO = None
CONSTRAINS = True
ROAD = '1'
SAVE_SCENARIO = False
REALISTIC = False
collision_tag = False
EFFECT_NAME = 'Default'
EPISODE = 0
MID_POINT = {0, 0, 0}
CONTROL = False
NPC_QUEUE = queue.Queue(maxsize=10)
collision_speed = 0  # 0 indicates there is no collision occurred.
collision_uid = "No collision"

speed_list = []

cars = ['Jeep', 'BoxTruck', 'Sedan', 'SchoolBus', 'SUV', 'Hatchback']
colors = ['pink', 'yellow', 'red', 'white', 'black', 'skyblue']

u = 0.6
z_axis = lgsvl.Vector(0, 0, 100)
prefix = '/deepqtest/lgsvl-api/'

APOLLO_HOST = '104.199.189.78'  # or 'localhost'
PORT = 8966

msg_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = (APOLLO_HOST, PORT)

msg_socket.connect(server_address)

# file_name = str(int(time.time()))
# title = ["x", "y", "z", "rain", "fog", "wetness", "timeofday", "signal", "rx", "ry", "rz", "speed",
#          "num_obs", "num_npc", "min_obs_dist", "speed_min_obs_dist", "vol_min_obs_dist", "dist_to_max_speed_obs",
#          "local_diff", "local_angle", "dis_diff", "theta_diff", "vel_diff", "size_diff",
#          "mlp_eval", 'cost_eval', 'cruise_mlp_eval', 'junction_mlp_eval', 'cyclist_keep_lane_eval', 'lane_scanning_eval',
#          'pedestrian_interaction_eval', 'junction_map_eval', 'lane_aggregating_eval', 'semantic_lstm_eval', 'jointly_prediction_planning_eval', 'vectornet_eval',
#          'unknown', 'throttle', 'brake', 'steering_rate', 'steering_target', 'acceleration', 'gear', 'collision_probability']

# pd.DataFrame([title]).to_csv('./log/state/state_' + file_name + '.csv', mode='w', header=False, index=None)

# apollo_host = "192.168.50.51"

"""
Util Functions
"""


def on_collision(agent1, agent2, contact):
    name1 = agent1.__dict__.get('name')
    name2 = agent2.__dict__.get('name') if agent2 is not None else "OBSTACLE"
    uid = agent2.__dict__.get('uid') if agent2 is not None else "OBSTACLE"
    print("{} collided with {} at {}".format(name1, name2, contact))
    global collision_object
    global collision_tag
    global collision_speed
    global collision_uid
    collision_uid = uid
    collision_object = name2
    collision_tag = True
    # raise evaluator.TestException("Ego collided with {}".format(agent2))
    try:
        collision_speed = agent1.state.speed
    except KeyError:
        collision_speed = -1
        print('KeyError')


def get_no_conflict_position(position, car):
    if car == 'BoxTruck' or car == 'SchoolBus':
        sd = 10
    else:
        sd = 8
    generate = True
    if CONSTRAINS:
        agents = sim.get_agents()
        for agent in agents:
            if math.sqrt(pow(position.x - agent.transform.position.x, 2) +
                         pow(position.y - agent.transform.position.y, 2) +
                         pow(position.z - agent.transform.position.z, 2)) < sd:
                generate = False
                break
    # if not CONSTRAINS:
    #     generate = True
    return generate

def set_color(color):
    colorV = lgsvl.Vector(0, 0, 0)
    if color == 'black':
        colorV = lgsvl.Vector(0, 0, 0)
    elif color == 'white':
        colorV = lgsvl.Vector(1, 1, 1)
    elif color == 'yellow':
        colorV = lgsvl.Vector(1, 1, 0)
    elif color == 'pink':
        colorV = lgsvl.Vector(1, 0, 1)
    elif color == 'skyblue':
        colorV = lgsvl.Vector(0, 1, 1)
    elif color == 'red':
        colorV = lgsvl.Vector(1, 0, 0)
    elif color == 'green':
        colorV = lgsvl.Vector(0, 1, 0)
    elif color == 'blue':
        colorV = lgsvl.Vector(0, 0, 1)
    return colorV


def control_agents_density(agent):
    if CONTROL:

        if NPC_QUEUE.full():
            sim.remove_agent(NPC_QUEUE.get())
            NPC_QUEUE.put(agent)
        else:
            NPC_QUEUE.put(agent)

def get_image(camera_name):
    for sensor in sensors:
        if sensor.name == camera_name:
            camera = sensor
    filename = os.path.dirname(os.path.realpath(
        __file__)) + '/Image/' + str(camera_name).replace(' ', '_') + '.jpg'
    # print(filename)
    camera.save(filename, quality=75)
    im = cv2.imread(filename, 1)
    # print(im.shape)
    # im = cv2.resize(im, (297, 582))
    im = im[400:800, 500:1020]
    cv2.imwrite("./Image/" + str(camera_name).replace(' ', '_') +
                "_cropped.jpg", im)
    im = im.astype(np.float32)
    im /= 255.0
    # print(im)
    # main_image_dict = {'index': im.tolist()}
    # main_json = json.dumps(main_image_dict)
    return im


# def save_image(sensor_name):
#     for sensor in sensors:
#         if sensor.name == sensor_name:
#             camera = sensor
#             break
#     tag = str(int(time.time()))
#     filename = os.path.dirname(os.path.realpath(__file__)) + '/Image/' + str(sensor_name).replace(' ',
#                                                                                                   '_') + '/' + tag + '.jpg'
#     print(filename)
#     camera.save(filename, quality=75)
#

def save_image(sensor_name):
    path = './Image/' + str(sensor_name).replace(' ', '_')
    if not os.path.exists(path):
        os.makedirs(path)
    for sensor in sensors:
        if sensor.name == sensor_name:
            camera = sensor
            break
    tag = str(int(time.time()))
    filename = os.path.dirname(os.path.realpath(__file__)) + '/Image/' + str(sensor_name).replace(' ',
                                                                                                  '_') + '/' + tag + '.jpg'
    # print(filename)
    camera.save(filename, quality=75)


def save_lidar():
    path = './Lidar'
    tag = str(int(time.time()))
    if not os.path.exists(path):
        os.mkdir(path)
    for sensor in sensors:
        if sensor.name == "Lidar":
            sensor.save(
                os.path.dirname(os.path.realpath(__file__)) + "/Lidar/lidar_" + tag + ".pcd")


def save_all():
    save_image('Main Camera')
    save_image('Left Camera')
    save_image('Right Camera')
    save_image('Left Segmentation Camera')
    save_image('Right Segmentation Camera')
    save_image('Segmentation Camera')
    save_lidar()


def get_type(class_name):
    # print(class_name)
    object_type = None
    if str(class_name) == '<class \'lgsvl.agent.EgoVehicle\'>':
        object_type = 'Ego'
    elif str(class_name) == '<class \'lgsvl.agent.Pedestrian\'>':
        object_type = 'Pedestrian'
    elif str(class_name) == '<class \'lgsvl.agent.NpcVehicle\'>':
        object_type = 'NPC'
    return object_type


def calculate_measures_thread(state_list, ego_state, ego_brake, pre_ego_brake, ego_acceleration,
                              ego_rotation, pre_ego_rotation, isNpcVehicle, TTC_list,
                              distance_list, probability_list, violation_list,
                              mid_point=None, collision_tag_=False):

    # global msg_socket
    # print("Start Threading")

    TTC, distance, probability2, violation_reward = calculate_measures(
        state_list, ego_state, ego_brake, pre_ego_brake, ego_acceleration,
        ego_rotation, pre_ego_rotation, isNpcVehicle, mid_point, True)
    # if SAVE_SCENARIO:
    # TTC, distance, probability2 = calculate_measures(agents, ego, SAVE_SCENARIO)
    # probability2 = get_collision_probability2(agents)
    TTC_list.append(round(TTC, 6))
    # print("Distance: ", distance)
    distance_list.append(round(distance, 6))
    if collision_tag_:
        probability2 = 1
    probability_list.append(round(probability2, 6))
    violation_reward[1] = probability2
    violation_list.append(violation_reward)
    # print("Stop Threading")


def calculate_metrics(agents, ego):
    global probability
    global DATETIME_UNIX
    global collision_tag

    global SAVE_SCENARIO
    # probability = 0

    # global uncomfortable_angularAcceleration
    global collision_object
    global collision_speed
    global TIMESTAMP
    global MID_POINT
    global collision_uid
    global msg_socket
    # global msg_socket

    # uncomfortable_angularAcceleration = [20, JERK_THRESHOLD, 2800]
    collision_object = None
    collision_speed = 0  # 0 indicates there is no collision occurred.
    collision_speed_ = 0
    collision_type = "None"
    collision_uid = "No collision"

    print("Calculating metrics ....")

    TTC_list = []
    distance_list = []
    probability_list = []
    violation_list = []
    i = 0
    time_step = 0.5
    speed = 0

    pre_ego_brake = 0
    pre_ego_rotation = 0

    if SAVE_SCENARIO:
        # print(sim.current_time)
        doc, root = initialization(
            '2022-11-11', get_time_stamp(), './{}.json'.format(EFFECT_NAME))
        entities, storyboard = initializeStory(agents, doc, root)
        story = doc.createElement('Story')
        story.setAttribute('name', 'Default')

    while i < observation_time / time_step:
        # """
        # Updating date time.
        # """
        # dt = sim.current_datetime
        # DATETIME_UNIX = time.mktime(dt.timetuple())
        #

        # print("Continue calculate metrics .... ", i)

        if SAVE_SCENARIO:
            # create scenarios each at time step
            create_story_by_timestamp(i + 1, doc, story, entities, agents, sim)

        # print("Observation_time: ", observation_time, "time_step: ", time_step)
        # print("Run sim ", i)

        sim.run(time_limit=time_step)  # , time_scale=2
        # msg_socket.send(json.dumps(["get_data"]).encode("utf-8"))

        # sim.run()
        # print("Run sim completely", i)
        # print('acc_new:', acc_new)
        # speed_new = EGO.state.speed
        # TTC, distance, probability2 = 100000, 100000, 0
        # TTC, distance, probability2 = calculate_measures(agents, ego, True)

        state_list = []
        isNpcVehicle = []
        for j in range(1, len(agents)):
            state_ = agents[j].state
            state_list.append(state_)
            isNpc = (isinstance(agents[j], NpcVehicle))
            isNpcVehicle.append(isNpc)

        ego_state = ego.state

        msg_socket.send(json.dumps(["get_acc"]).encode("utf-8"))
        data = msg_socket.recv(2048)

        data = json.loads(data.decode("utf-8"))

        ego_brake = data["brake"]
        ego_acceleration = data["acceleration"]

        # TTC, distance, probability2 = calculate_measures(
        # state_list, ego_state, isNpcVehicle, MID_POINT, True)
        # # if SAVE_SCENARIO:
        # # TTC, distance, probability2 = calculate_measures(agents, ego, SAVE_SCENARIO)
        # # probability2 = get_collision_probability2(agents)
        # TTC_list.append(round(TTC, 6))
        # # print("Distance: ", distance)
        # distance_list.append(round(distance, 6))
        # if collision_tag:
        #     probability2 = 1
        # probability_list.append(round(probability2, 6))

        # r = requests.get("http://localhost:8933/LGSVL/Status/Environment/State")
        # a = r.json()
        # items = a.items()
        # state_ = [value for key, value in items]
        # state_.append(probability2)
        # pd.DataFrame([state_]).to_csv('./log/state/state_' + file_name + '.csv', mode='a', header=False, index=None)

        thread = threading.Thread(
            target=calculate_measures_thread,
            args=(state_list, ego_state, ego_brake, pre_ego_brake, ego_acceleration,
                  ego_state.rotation.y, pre_ego_rotation, isNpcVehicle, TTC_list, distance_list,
                  probability_list, violation_list, MID_POINT, collision_tag,)
        )

        thread.start()

        pre_ego_brake = ego_brake
        pre_ego_rotation = ego_state.rotation.y

        # time.sleep(1)
        # if SAVE_SCENARIO:
        # TTC, distance, probability2 = calculate_measures(agents, ego, SAVE_SCENARIO)
        # probability2 = get_collision_probability2(agents)
        # TTC_list.append(round(TTC, 6))
        # print("Distance: ", distance)
        # distance_list.append(round(distance, 6))
        if collision_tag:
            collision_tag = False

        # probability_list.append(round(probability2, 6))
        i += 1

    # if SAVE_SCENARIO:
    collision_type, collision_speed_, collision_uid_ = get_collision_info()
    if collision_speed_ == -1:
        collision_speed_ = speed

    if SAVE_SCENARIO:
        storyboard.appendChild(story)
        root.appendChild(storyboard)
        root.setAttribute('timestep', '0.5')

        path = '../../DeepQTestExperiment/Road{}'.format(ROAD)
        try:
            os.makedirs(path)
        except Exception as e:
            print(e)

        time_t = str(int(time.time()))

        fp = open(
            path + "/{}_Scenario_{}.deepscenario".format(EPISODE, time_t), "w")
        # fp = open('./Scenario_{}.deepscenario'.formconnect-dreamviewat(TIMESTAMP), 'w')
        doc.writexml(fp, addindent='\t', newl='\n', encoding="utf-8")

    # print({'TTC': TTC_list, 'distance': distance_list, 'collision_type': collision_type,
    #        'collision_speed': collision_speed_, 'JERK': JERK_list, 'probability': probability_list})

    print("Probability List: ", probability_list)
    # print("Violation list: ", violation_list)

    probability = round(max(probability_list), 6)
    violation_reward = np.max(violation_list, axis=0)

    print("Violation_reward: ", violation_reward)
    # print("Collision Probability:", probability)
    return {'TTC': TTC_list, 'distance': distance_list, 'collision_type': collision_type, 'collision_uid': collision_uid_,
            'collision_speed': collision_speed_, 'probability': probability_list, 'not_stop_pedes': violation_reward[0],
            'sudden_braking': violation_reward[2], 'improper_passing': violation_reward[3], 'improper_lane_changing': violation_reward[4]}  # 'uncomfortable': uncomfortable,


@app.route('/LGSVL')
def index():
    return "RESTful APIs for LGSVL simulator control."


@app.route('/LGSVL/get-datetime', methods=['GET'])
def get_time_stamp():
    # timeofday = round(sim.time_of_day)
    # if timeofday == 24:
    #     timeofday = 0
    # dt = sim.current_time
    # # print(dt)
    # dt = dt.replace(hour=timeofday, minute=0, second=0)
    # # print(int(time.mktime(dt.timetuple())))
    # return json.dumps(int(time.mktime(dt.timetuple())))
    return json.dumps(int(time.time()))


@app.route('/LGSVL/Episode', methods=['POST'])
def get_effect_info():
    global EPISODE
    EPISODE = int(request.args.get('episode'))

    # print('episode: ', EPISODE)

    return 'set effect-episode'


@app.route('/LGSVL/ego/collision_info', methods=['GET'])
def get_collision_info():
    """
    three types of collision: obstacle, NPC vehicle, pedestrian, None(no collision)
    :return:
    """
    global collision_object
    global collision_speed
    global collision_uid
    # global uncomfortable_angularAcceleration
    global JERK

    collision_info = str(collision_object)
    # print(collision_info)
    collision_speed_ = collision_speed
    # uncomfortable = uncomfortable_angularAcceleration

    collision_object = None
    collision_speed = 0
    # uncomfortable_angularAcceleration = [20, JERK_THRESHOLD, 2800]
    JERK = 0
    # convert collision information to one of three types
    collision_type = 'None'
    if collision_info == 'OBSTACLE':
        collision_type = "obstacle"
    if collision_info in npc_vehicle:
        collision_type = "npc_vehicle"
    if collision_info in pedestrian:
        collision_type = "pedestrian"
    return collision_type, collision_speed_, collision_uid


@app.route('/LGSVL/SetObTime', methods=['POST'])
def set_time():
    global observation_time
    observation_time = int(request.args.get('observation_time'))
    print(observation_time)
    return 'get time'


"""
Command APIs
"""


@app.route('/LGSVL/LoadScene', methods=['POST'])
def load_scene():
    global sensors
    global EGO
    global ROAD
    print('obTime: ', observation_time)
    scene = str(request.args.get('scene'))
    road_num = str(request.args.get('road_num'))
    ROAD = str(road_num)
    if sim.current_scene == scene:
        sim.reset()
    else:
        sim.load(scene)

    # controllables = sim.get_controllables("signal")
    # for c in controllables:
    #     c.control("green=3;loop")

    EGO = None
    # spawns = sim.get_spawn()
    state = lgsvl.AgentState()
    # state.transform = spawns[0]
    roadTransform_start = open(
        'Transform/transform-road' + road_num + '-start', 'rb')
    # state.transform = pickle.load(roadTransform_start)
    state.transform = torch.load(
        './Transform/{}.pt'.format("road" + "4" + "_start"))
    if road_num == '1':
        if scene == 'bd77ac3b-fbc3-41c3-a806-25915c777022':
            state.transform.position.x = -545.5
            state.transform.position.y = 36.0
            state.transform.position.z = 17.1
            state.transform.rotation.y = 127
            state.transform.rotation.x = 358
        elif scene == '45dd30b0-4be1-4eb7-820a-3e175b0117fc':
            state.transform.position.x = -698.2
            state.transform.position.y = 0.0
            state.transform.position.z = 1498.1
            state.transform.rotation.y = 289
            state.transform.rotation.x = 0
        elif scene == '5d272540-f689-4355-83c7-03bf11b6865f':
            state.transform.position.x = -598.6
            state.transform.position.y = 30.2
            state.transform.position.z = 207.4
            state.transform.rotation.y = 164
            state.transform.rotation.x = 0
        else:
            state.transform.position.x = -40.3
            state.transform.position.y = -1.4
            state.transform.position.z = -11.8
            state.transform.rotation.y = 105
            state.transform.rotation.x = 1
    elif road_num == '2':
        state.transform.position.x = 174.2
        state.transform.position.y = -4.7
        state.transform.position.z = -65.6
        state.transform.rotation.y = 105
    elif road_num == '3':
        state.transform.position.x = 273.8
        state.transform.position.y = -6.4
        state.transform.position.z = -91.8
        state.transform.rotation.y = 105
    elif road_num == '4':
        state.transform.position.x = 365.4
        state.transform.position.y = -7.8
        state.transform.position.z = 0.7
        state.transform.rotation.y = 15

    forward = lgsvl.utils.transform_to_forward(state.transform)

    state.velocity = 3 * forward


    # print(pickle.load(roadTransform_start))

    EGO = sim.add_agent("8e776f67-63d6-4fa3-8587-ad00a0b41034",
                        lgsvl.AgentType.EGO, state)
    EGO.connect_bridge(os.environ.get("BRIDGE_HOST", APOLLO_HOST), 9090)
    # print(len(sim.get_agents()))

    # speed = ego.state.speed
    # velocity = ego.state.velocity

    sensors = EGO.get_sensors()
    sim.get_agents()[0].on_collision(on_collision)

    # ss = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    data = {'road_num': road_num}

    if road_num == '1':
        if scene == 'bd77ac3b-fbc3-41c3-a806-25915c777022':
            requests.post(
                "http://localhost:8933/LGSVL/SetDestination?des_x=-283.0&des_y=34.4&des_z=-71.4")
        elif scene == '45dd30b0-4be1-4eb7-820a-3e175b0117fc':
            requests.post(
                "http://localhost:8933/LGSVL/SetDestination?des_x=-888.4&des_y=0.0&des_z=1559.9")
        elif scene == '5d272540-f689-4355-83c7-03bf11b6865f':
            requests.post(
                "http://localhost:8933/LGSVL/SetDestination?des_x=-550.8&des_y=30.2&des_z=-25.6")
        else:
            requests.post(
                "http://localhost:8933/LGSVL/SetDestination?des_x=348.2&des_y=-7.5&des_z=-64.4")
    elif road_num == '2':
        requests.post(
            "http://localhost:8933/LGSVL/SetDestination?des_x=268.5&des_y=-7.0&des_z=-118.9")
    elif road_num == '3':
        requests.post(
            "http://localhost:8933/LGSVL/SetDestination?des_x=351.9&des_y=-7.7&des_z=-50.3")
    elif road_num == '4':
        requests.post(
            "http://localhost:8933/LGSVL/SetDestination?des_x=354.2&des_y=-7.7&des_z=74.8")

    print(road_num)

    # ss_route.send(json.dumps(data).encode('utf-8'))
    # ss.close()
    roadTransform_start.close()
    sim.run(2)

    # print("In the bottom of LoadScene", len(sim.get_agents()))f

    return 'load success'


@app.route('/LGSVL/SaveTransform', methods=['POST'])
def save_transform():
    transform = sim.get_agents()[0].state.transform
    f = open('Transform/transform2', 'wb')
    pickle.dump(transform, f)
    return 'saved'


"""
Sim run
"""


@app.route('/LGSVL/Run', methods=['POST'])
def run():
    sim.run(8)
    return 'sim run'


"""
Randomly Load Agents
"""


@app.route('/LGSVL/LoadNPCVehicleRandomly', methods=['POST'])
def load_npc_vehicle_randomly():
    sim.add_random_agents(lgsvl.AgentType.NPC)
    return 'NPC Loaded'


@app.route('/LGSVL/LoadPedestriansRandomly', methods=['POST'])
def load_pedestrians_randomly():
    sim.add_random_agents(lgsvl.AgentType.PEDESTRIAN)
    sim.run(6)
    # sim.run(5)
    return "Pedestrians Loaded"


@app.route('/LGSVL/Reset', methods=['POST'])
def reset_env():
    state = lgsvl.AgentState()
    state.transform = sim.get_agents()[0].state.transform
    sim.reset()
    ego = sim.add_agent("8e776f67-63d6-4fa3-8587-ad00a0b41034",
                        lgsvl.AgentType.EGO, state)
    # ego.transform.position
    ego.connect_bridge(os.environ.get("BRIDGE_HOST", APOLLO_HOST), 9090)
    global sensors
    sensors = ego.get_sensors()
    sim.get_agents()[0].on_collision(on_collision)
    # sim.run(5)
    sim.run(6)
    return "reset"


"""
Reset Agent
"""


@app.route('/LGSVL/EGOVehicle/Reset', methods=['POST'])
def clear_env():
    # FIXME, UNUSED
    # spawns = sim.get_spawn()
    # # sim.get_agents().
    # state = lgsvl.AgentState()
    # state.transform = spawns[0]
    # ego = sim.add_agent("Lincoln2017MKZ (Apollo 5.0)", lgsvl.AgentType.EGO, state)
    # sim.run()
    agents = sim.get_agents()
    # for agent in agents:
    for i in range(1, len(agents)):
        sim.remove_agent(agents[i])
    # sim.remove_agent(agents[1])
    # sim.reset()
    sim.run(6)
    return 'reset'


@app.route('/LGSVL/SaveState', methods=['POST'])
def save_state():
    state_id = str(request.args.get('ID'))

    agents = sim.get_agents()
    count_ego = 0
    count_npc = 0
    count_pedestrian = 0

    states_dict = {}

    weather_dict = {}

    weather_dict.update(
        {'rain': sim.weather.rain, 'fog': sim.weather.fog, 'wetness': sim.weather.wetness, 'time': sim.time_of_day})

    for agent in agents:
        obj_name = "None"
        obj_uid = agent.uid
        print(obj_uid, type(agent.uid))
        obj_color_vector = "Vector(1, 1, 0)"
        obj_type = get_type(agent.__class__)
        if obj_type == 'Ego':
            obj_name = 'Ego' + str(count_ego)
            # obj_color_vector = str(agent.color)
            count_ego += 1
        elif obj_type == 'NPC':
            obj_name = 'NPC' + str(count_npc)
            count_npc += 1
            # obj_color_vector = str(agent.color)
        elif obj_type == 'Pedestrian':
            obj_name = 'Pedestrian' + str(count_pedestrian)
            # obj_color_vector = str(agent.color)
        model = agent.name

        agent_dict = {}
        agent_dict.update({'model': model, 'name:': obj_name,
                          'obj_color': obj_color_vector})
        agent_dict.update({'positionX': agent.state.position.x, 'positionY': agent.state.position.y,
                           'positionZ': agent.state.position.z})
        agent_dict.update({'rotationX': agent.state.rotation.x, 'rotationY': agent.state.rotation.y,
                           'rotationZ': agent.state.rotation.z})
        agent_dict.update({'velocityX': agent.state.velocity.x, 'velocityY': agent.state.velocity.y,
                           'velocityZ': agent.state.velocity.z})
        agent_dict.update(
            {'angularVelocityX': agent.state.angular_velocity.x, 'angularVelocityY': agent.state.angular_velocity.y,
             'angularVelocityZ': agent.state.angular_velocity.z})

        states_dict.update({obj_uid: agent_dict})

    states_dict.update({'worldEffect': weather_dict})

    b = json.dumps(states_dict, indent=4)
    file = open('state/current_state_{}.json'.format(state_id), 'w')
    file.write(b)
    file.close()
    return 'save successfully'


@app.route('/LGSVL/SetDestination', methods=['POST'])
def set_destination():
    global DESTINATION
    global EGO
    # enable_modules()
    x = float(request.args.get('des_x'))
    y = float(request.args.get('des_y'))
    z = float(request.args.get('des_z'))
    print("x y z: ", x, y, z)
    DREAMVIEW = lgsvl.dreamview.Connection(sim, EGO, APOLLO_HOST)
    # DESTINATION = (x, y, z)
    DREAMVIEW.set_destination(x, z, y, coord_type=CoordType.Unity)
    return 'set destination.'


@app.route('/LGSVL/RollBack', methods=['POST'])
def roll_back():
    state_ID = str(request.args.get('ID'))
    # print(state_ID)
    state = open('state/current_state_{}.json'.format(state_ID), 'r')
    content = state.read()
    state_ = json.loads(content)
    sim.weather = lgsvl.WeatherState(rain=state_['worldEffect']['rain'], fog=state_['worldEffect']['fog'],
                                     wetness=state_['worldEffect']['wetness'])
    sim.set_time_of_day(state_['worldEffect']['time'])

    # print("middle of Rollback: ", len(sim.get_agents()))

    for agent in sim.get_agents():
        if agent.uid not in state_.keys():
            sim.remove_agent(agent)
            continue
        agent_state = state_[agent.uid]
        position = lgsvl.Vector(float(agent_state['positionX']), float(agent_state['positionY']),
                                float(agent_state['positionZ']))
        rotation = lgsvl.Vector(float(agent_state['rotationX']), float(agent_state['rotationY']),
                                float(agent_state['rotationZ']))
        velocity = lgsvl.Vector(float(agent_state['velocityX']), float(agent_state['velocityY']),
                                float(agent_state['velocityZ']))
        angular_velocity = lgsvl.Vector(float(agent_state['angularVelocityX']), float(agent_state['angularVelocityY']),
                                        float(agent_state['angularVelocityZ']))
        state = lgsvl.AgentState()
        state.transform.position = position
        state.transform.rotation = rotation
        state.velocity = velocity
        state.angular_velocity = angular_velocity
        agent.state = state

    # print("In the bottom of Rollback: ", len(sim.get_agents()))

    return 'rollback'


"""
Set weather effect
"""


@app.route('/LGSVL/Control/Weather/Nice', methods=['POST'])
def nice():
    global REALISTIC
    sim.weather = lgsvl.WeatherState(rain=0, fog=0, wetness=0)
    REALISTIC = False
    print('realistic constraints: ', REALISTIC)

    agents = sim.get_agents()
    ego = agents[0]

    return calculate_metrics(agents, ego)


@app.route('/LGSVL/Control/Weather/Rain', methods=['POST'])
def rain():
    """
    three parameters: Light, Moderate and Heavy,
    apparently, wetness will be influenced.
    :return:
    """
    global REALISTIC
    rain_level = request.args.get('rain_level')
    # print(rain_level, 'rain')
    r_level = 0
    w_level = 0
    if rain_level == 'Light':
        r_level = 0.2
        w_level = 0.2

    elif rain_level == 'Moderate':
        r_level = 0.5
        w_level = 0.5
    elif rain_level == 'Heavy':
        r_level = 1
        w_level = 1
    sim.weather = lgsvl.WeatherState(rain=r_level, fog=0, wetness=w_level)
    # sim.get_agents()[0].on_collision(on_collision)
    REALISTIC = False
    print('realistic constraints: ', REALISTIC)

    agents = sim.get_agents()
    ego = agents[0]

    return calculate_metrics(agents, ego)


@app.route('/LGSVL/Control/Weather/Fog', methods=['POST'])
def fog():
    """
    three parameters: Light, Moderate and Heavy
    :return:
    """
    global REALISTIC
    fog_level = request.args.get('fog_level')
    # print(fog_level, 'fog')
    f_level = 0
    if fog_level == 'Light':
        f_level = 0.2
    elif fog_level == 'Moderate':
        f_level = 0.5
    elif fog_level == 'Heavy':
        f_level = 1
    sim.weather = lgsvl.WeatherState(rain=0, fog=f_level, wetness=0)
    # sim.get_agents()[0].on_collision(on_collision)
    REALISTIC = False
    print('realistic constraints: ', REALISTIC)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route('/LGSVL/Control/Weather/Wetness', methods=['POST'])
def wetness():
    """
    three parameters: Light, Moderate and Heavy
    :return:
    """
    global REALISTIC
    wetness_level = request.args.get('wetness_level')
    # print(wetness_level, 'wetness')
    w_level = 0
    if wetness_level == 'Light':
        w_level = 0.2
    elif wetness_level == 'Moderate':
        w_level = 0.5
    elif wetness_level == 'Heavy':
        w_level = 1
    sim.weather = lgsvl.WeatherState(rain=0, fog=0, wetness=w_level)
    # sim.get_agents()[0].on_collision(on_collision)
    REALISTIC = False
    print('realistic constraints: ', REALISTIC)
    agents = sim.get_agents()
    ego = agents[0]

    return calculate_metrics(agents, ego)


"""
Set time of day
"""


@app.route('/LGSVL/Control/TimeOfDay', methods=['POST'])
def time_of_day():
    """
    three parameters: Morning(10), Noon(14), Evening(20)
    :return:
    """
    global REALISTIC
    time = request.args.get('time_of_day')
    # print(time)
    day_time = 10  # initial time: 10
    if time == 'Morning':
        day_time = 10
    elif time == 'Noon':
        day_time = 14
    elif time == 'Evening':
        day_time = 20
    sim.set_time_of_day(day_time, fixed=True)
    # sim.get_agents()[0].on_collision(on_collision)
    REALISTIC = False
    print('realistic constraints: ', REALISTIC)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


"""
Control Agents
"""


@app.route(prefix + 'agents/npc-vehicle/cross-road', methods=['POST'])
def add_npc_cross_road():
    global NPC_QUEUE
    global cars
    global colors
    which_car = cars[random.randint(0, 5)]
    color = colors[random.randint(0, 5)]
    change_lane = int(request.args.get('maintainlane'))
    distance = str(request.args.get('position'))
    colorV = set_color(color)

    ego_transform = sim.get_agents()[0].state.transform
    sx = ego_transform.position.x
    sy = ego_transform.position.y
    sz = ego_transform.position.z

    angle = math.pi
    dist = 20 if distance == 'near' else 50

    point = lgsvl.Vector(sx + dist * math.cos(angle), sy, sz + dist * math.sin(angle))
    state = lgsvl.AgentState()
    state.transform = sim.map_point_on_lane(point)

    generate = get_no_conflict_position(state.position, which_car)
    if generate:
        npc = sim.add_agent(which_car, lgsvl.AgentType.NPC, state, colorV)
        npc.follow_closest_lane(True, 20)
        npc.change_lane(change_lane == 1)

        control_agents_density(npc)
    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route(prefix + 'agents/pedestrian/cross-road', methods=['POST'])
def add_pedestrian_cross_road():
    global NPC_QUEUE
    direction = request.args.get('direction')
    ego_transform = sim.get_agents()[0].state.transform
    forward = lgsvl.utils.transform_to_forward(ego_transform)
    right = lgsvl.utils.transform_to_right(ego_transform)
    npc_state = lgsvl.AgentState()

    if direction == 'left':
        offset = - 5.0 * right
    else:
        offset = 5.0 * right

    wp = [lgsvl.WalkWaypoint(sim.map_point_on_lane(ego_transform.position + offset + 30 * forward).position, 1),
          lgsvl.WalkWaypoint(sim.map_point_on_lane(ego_transform.position - offset + 30 * forward).position, 1)]

    npc_state.transform.position = sim.map_point_on_lane(ego_transform.position + offset + 30.0 * forward).position

    generate = get_no_conflict_position(npc_state.transform.position, 'pedestrian')
    if generate:
        name = pedestrian[random.randint(0, 8)]
        p = sim.add_agent(name, lgsvl.AgentType.PEDESTRIAN, npc_state)
        p.follow(wp, loop=False)

        control_agents_density(p)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route(prefix + 'agents/npc-vehicle/drive-ahead', methods=['POST'])
def add_npc_drive_ahead():
    global NPC_QUEUE
    global cars
    global colors
    which_lane = request.args.get('lane')
    which_car = cars[random.randint(0, 5)]
    color = colors[random.randint(0, 5)]
    change_lane = int(request.args.get('maintainlane'))
    distance = str(request.args.get('position'))
    colorV = set_color(color)

    ego_transform = sim.get_agents()[0].state.transform
    forward = lgsvl.utils.transform_to_forward(ego_transform)
    right = lgsvl.utils.transform_to_right(ego_transform)

    if distance == 'near':
        offset = 10
        if which_car == 'BoxTruck' or which_car == 'SchoolBus':
            forward = 15 * forward
            right = 4 * right
            speed = 10
        else:
            forward = 12 * forward
            right = 4 * right
            speed = 10
    else:
        offset = 50
        if which_car == 'BoxTruck' or which_car == 'SchoolBus':
            forward = 50 * forward
            right = 4 * right
            speed = 10
        else:
            forward = 50 * forward
            right = 4 * right
            speed = 10

    if which_lane == "left":
        point = ego_transform.position - right + forward
    elif which_lane == "right":
        point = ego_transform.position + right + forward
    elif which_lane == "current":
        point = ego_transform.position + forward
    else:
        point = lgsvl.Vector(ego_transform.position.x + offset * math.cos(0), ego_transform.position.y,
                             ego_transform.position.z + offset * math.sin(0))

    npc_state = lgsvl.AgentState()
    npc_state.transform = sim.map_point_on_lane(point)

    generate = get_no_conflict_position(npc_state.position, which_car)
    if generate:
        npc = sim.add_agent(which_car, lgsvl.AgentType.NPC, npc_state, colorV)
        npc.follow_closest_lane(True, speed)
        npc.change_lane(change_lane == 1)

        control_agents_density(npc)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route(prefix + 'agents/npc-vehicle/overtake', methods=['POST'])
def add_npc_overtake():
    global NPC_QUEUE
    global cars
    global colors
    which_lane = request.args.get('lane')
    which_car = cars[random.randint(0, 5)]
    color = colors[random.randint(0, 5)]
    change_lane = int(request.args.get('maintainlane'))
    distance = str(request.args.get('position'))
    colorV = set_color(color)

    ego_transform = sim.get_agents()[0].state.transform

    forward = lgsvl.utils.transform_to_forward(ego_transform)
    right = lgsvl.utils.transform_to_right(ego_transform)

    if distance == 'near':
        offset = 10
        if which_car == 'BoxTruck' or which_car == 'SchoolBus':
            forward = 20 * forward
            right = 5 * right
            speed = 20
        else:
            forward = 10 * forward
            right = 4 * right
            speed = 30
    else:
        offset = 50
        if which_car == 'BoxTruck' or which_car == 'SchoolBus':
            forward = 50 * forward
            right = 5 * right
            speed = 20
        else:
            forward = 50 * forward
            right = 4 * right
            speed = 30

    if which_lane == "left":
        point = ego_transform.position - right - forward
    elif which_lane == "right":
        point = ego_transform.position + right - forward
    elif which_lane == "current":
        point = ego_transform.position - forward
    else:
        point = lgsvl.Vector(ego_transform.position.x + offset * math.cos(0), ego_transform.position.y,
                             ego_transform.position.z + offset * math.sin(0))

    npc_state = lgsvl.AgentState()
    npc_state.transform = sim.map_point_on_lane(point)

    generate = get_no_conflict_position(npc_state.position, which_car)
    if generate:
        npc = sim.add_agent(which_car, lgsvl.AgentType.NPC, npc_state, colorV)
        npc.follow_closest_lane(True, speed)
        npc.change_lane(change_lane == 1)

        control_agents_density(npc)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route(prefix + 'agents/npc-vehicle/drive-opposite', methods=['POST'])
def add_npc_drive_opposite():
    global NPC_QUEUE
    which_car = cars[random.randint(0, 5)]
    color = colors[random.randint(0, 5)]
    change_lane = int(request.args.get('maintainlane'))
    distance = str(request.args.get('position'))
    colorV = set_color(color)

    ego_transform = sim.get_agents()[0].state.transform
    forward = lgsvl.utils.transform_to_forward(ego_transform)
    right = lgsvl.utils.transform_to_right(ego_transform)

    if distance == 'near':
        offset = 20
    else:
        offset = 50

    if which_car == 'BoxTruck' or which_car == 'SchoolBus':
        forward = offset * forward
        right = 8 * right
        speed = 20
    else:
        forward = offset * forward
        right = 8 * right
        speed = 20

    point = ego_transform.position - right + forward

    npc_state = lgsvl.AgentState()
    npc_state.transform = sim.map_point_on_lane(point)

    generate = get_no_conflict_position(npc_state.position, which_car)
    if generate:
        npc = sim.add_agent(which_car, lgsvl.AgentType.NPC, npc_state, colorV)
        npc.follow_closest_lane(True, speed)
        npc.change_lane(change_lane == 1)

        control_agents_density(npc)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


@app.route('/LGSVL/Control/ControllableObjects/TrafficLight', methods=['POST'])
def control_traffic_light():
    # FIXME
    ego_transform = sim.get_agents()[0].state.transform
    forward = lgsvl.utils.transform_to_forward(ego_transform)
    position = ego_transform.position + 50.0 * forward
    signal = sim.get_controllable(position, "signal")

    # Create a new control policy
    control_policy = "trigger=100;red=5"
    signal.control(control_policy)
    # print(signal.current_state)
    # sim.get_agents()[0].on_collision(on_collision)
    # sim.run(5)

    agents = sim.get_agents()
    ego = agents[0]
    return calculate_metrics(agents, ego)


"""
Status APIs
"""


def interpreter_signal(signal_state):
    code = 0
    if signal_state == 'red':
        code = -1
    elif signal_state == 'yellow':
        code = 0
    elif signal_state == 'green':
        code = 1
    return code


def get_apollo_msg():
    global msg_socket

    msg_socket.send(json.dumps(["start_getting_data"]).encode("utf-8"))
    data = msg_socket.recv(2048)

    data = json.loads(data.decode("utf-8"))

    control_info = data["control_info"]
    local_info = data["local_info"]
    pred_info = data["pred_info"]
    per_info = data["per_info"]
    # pred_confi = data["pred_ìno"]

    return local_info, per_info, pred_info, control_info
    # return pred_info

def get_confidence_data():
    global msg_socket

    msg_socket.send(json.dumps(["get_confidence_data"]).encode("utf-8"))
    data = msg_socket.recv(1024)
    data = json.loads(data.decode("utf-8"))

    # per_avg = data["per_avg"]
    # per_confi = data["per_confi"]
    # pred_confi = data["pred_confi"]

    return per_avg, per_confi, pred_confi

def cal_dis(x_a, y_a, z_a, x_b, y_b, z_b):
    return math.sqrt((x_a - x_b) ** 2 + (y_a - y_b) ** 2 + (z_a - z_b) ** 2)

@app.route('/LGSVL/Status/Environment/State', methods=['GET'])
def get_environment_state():
    global MID_POINT
    # save_image('Main Camera')
    # save_image('Segmentation Camera')
    # Add four new state: rotation <x, y, z>, speed

    # state[0] = position.x
    # state[1] = position.y
    # state[2] = position.z
    # state[3] = rotation.x
    # state[4] = rotation.y
    # state[5] = rotation.z
    # state[6] = weather.rain
    # state[7] = weather.fog
    # state[8] = weather.wetness
    # state[9] = sim.time_of_day
    # state[10] = interpreter_signal(signal.current_state)
    # state[11] = speed

    agents = sim.get_agents()

    weather = sim.weather
    position = agents[0].state.position
    rotation = agents[0].state.rotation
    signal = sim.get_controllable(position, "signal")
    speed = agents[0].state.speed

    # calculate advanced external features

    num_obs = len(agents) - 1
    num_npc = 0

    min_obs_dist = 100000
    speed_min_obs_dist = 1000
    vol_min_obs_dist = 1000
    dist_to_max_speed_obs = 100000

    max_speed = -100000

    for j in range(1, num_obs + 1):
        state_ = agents[j].state
        if isinstance(agents[j], NpcVehicle):
            num_npc += 1

        dis_to_ego = cal_dis(position.x,
                             position.y,
                             position.z,
                             state_.position.x,
                             state_.position.y,
                             state_.position.z)

        if dis_to_ego < min_obs_dist:
            min_obs_dist = dis_to_ego
            speed_min_obs_dist = state_.speed

            bounding_box_agent = agents[j].bounding_box
            size = bounding_box_agent.size
            vol = size.x * size.y * size.z
            vol_min_obs_dist = vol

        if max_speed < state_.speed:
            max_speed = state_.speed
            dist_to_max_speed_obs = dis_to_ego

    # get apollo info
    local_info, per_info, pred_info, control_info = get_apollo_msg()

    # transform ego's position to world coordinate position
    transform = lgsvl.Transform(
        lgsvl.Vector(position.x, position.y,
                     position.z), lgsvl.Vector(1, 105, 0)
    )
    gps = sim.map_to_gps(transform)
    dest_x = gps.easting
    dest_y = gps.northing

    # Calculate the differences between localization and simulator

    vector_avut = np.array([
        dest_x,
        dest_y,
    ])

    vector_local = np.array([
        local_info['position']['x'],
        local_info['position']['y'],
    ])

    local_diff = np.linalg.norm(vector_local - vector_avut)

    # Specify the mid point between localization's position and simulator ego's position

    vector_mid = np.array([
        (vector_avut[0] + vector_local[0]) / 2,
        (vector_avut[1] + vector_local[1]) / 2
    ])

    gps2 = sim.map_from_gps(
        None, None, vector_mid[1], vector_mid[0], None, None)

    MID_POINT = np.array([
        gps2.position.x,
        gps2.position.y,
        gps2.position.z
    ])

    # Calculate the angle of lcoalization's position and simulator ego's position

    v_x = vector_local[0] - vector_avut[0]
    v_y = vector_local[1] - vector_avut[1]

    local_angle = math.atan2(v_y, v_x)

    if v_x < 0:
        local_angle += math.pi

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'throttle': control_info['throttle'], 'brake': control_info['brake'],
    #               'steering_rate': control_info['steering_rate'], 'steering_target': control_info['steering_target'],
    #               'acceleration': control_info['acceleration'], 'gear': control_info['gear']}

    state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
                  'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
                  'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
                  'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
                  'speed': speed, 'local_diff': local_diff, 'local_angle': local_angle,
                  'dis_diff': per_info["dis_diff"], 'theta_diff': per_info["theta_diff"],
                  'vel_diff': per_info["vel_diff"], 'size_diff': per_info["size_diff"],
                  'mlp_eval': pred_info['mlp_eval'], 'cost_eval': pred_info['cost_eval'],
                  'cruise_mlp_eval': pred_info['cruise_mlp_eval'],
                  'junction_mlp_eval': pred_info['junction_mlp_eval'],
                  'cyclist_keep_lane_eval': pred_info['cyclist_keep_lane_eval'],
                  'lane_scanning_eval': pred_info['lane_scanning_eval'],
                  'pedestrian_interaction_eval': pred_info['pedestrian_interaction_eval'],
                  'junction_map_eval': pred_info['junction_map_eval'],
                  'lane_aggregating_eval': pred_info['lane_aggregating_eval'],
                  'semantic_lstm_eval': pred_info['semantic_lstm_eval'],
                  'jointly_prediction_planning_eval': pred_info['jointly_prediction_planning_eval'],
                  'vectornet_eval': pred_info['vectornet_eval'],
                  'unknown': pred_info['unknown'], 'throttle': control_info['throttle'],
                  'brake': control_info['brake'], 'steering_rate': control_info['steering_rate'],
                  'steering_target': control_info['steering_target'], 'acceleration': control_info['acceleration'],
                  'gear': control_info['gear'], "num_obs": num_obs, "num_npc": num_npc,
                  "min_obs_dist": min_obs_dist, "speed_min_obs_dist": speed_min_obs_dist,
                  "vol_min_obs_dist": vol_min_obs_dist, "dist_to_max_speed_obs": dist_to_max_speed_obs
                }

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'dis_diff': per_info["dis_diff"], 'theta_diff': per_info["theta_diff"],
    #               'vel_diff': per_info["vel_diff"], 'size_diff': per_info["size_diff"],
    #               }

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'local_diff': local_diff, 'local_angle': local_angle}

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'mlp_eval': pred_info['mlp_eval'],
    #               'cost_eval': pred_info['cost_eval'],
    #               'cruise_mlp_eval': pred_info['cruise_mlp_eval'],
    #               'junction_mlp_eval': pred_info['junction_mlp_eval'],
    #               'cyclist_keep_lane_eval': pred_info['cyclist_keep_lane_eval'],
    #               'lane_scanning_eval': pred_info['lane_scanning_eval'],
    #               'pedestrian_interaction_eval': pred_info['pedestrian_interaction_eval'],
    #               'junction_map_eval': pred_info['junction_map_eval'],
    #               'lane_aggregating_eval': pred_info['lane_aggregating_eval'],
    #               'semantic_lstm_eval': pred_info['semantic_lstm_eval'],
    #               'jointly_prediction_planning_eval': pred_info['jointly_prediction_planning_eval'],
    #               'vectornet_eval': pred_info['vectornet_eval'],
    #               'unknown': pred_info['unknown']
    #               }

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed}

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, "num_obs": num_obs, "num_npc": num_npc,
    #               "min_obs_dist": min_obs_dist, "speed_min_obs_dist": speed_min_obs_dist,
    #               "vol_min_obs_dist": vol_min_obs_dist, "dist_to_max_speed_obs": dist_to_max_speed_obs}

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'per': per_confi, 'pred': pred_confi}

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rx': rotation.x, 'ry': rotation.y, 'rz': rotation.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state),
    #               'speed': speed, 'local': local_diff, 'per': per_confi, 'pred': pred_confi}

    # state_dict = {'x': position.x, 'y': position.y, 'z': position.z,
    #               'rain': weather.rain, 'fog': weather.fog, 'wetness': weather.wetness,
    #               'timeofday': sim.time_of_day, 'signal': interpreter_signal(signal.current_state)}
    return json.dumps(state_dict)


@app.route('/LGSVL/Status/Realistic', methods=['GET'])
def get_realistic():

    return json.dumps(REALISTIC)


@app.route('/LGSVL/Status/Environment/Weather', methods=['GET'])
def get_weather():
    weather = sim.weather
    weather_dict = {'rain': weather.rain,
                    'fog': weather.fog, 'wetness': weather.wetness}

    return json.dumps(weather_dict)


@app.route('/LGSVL/Status/Environment/Weather/Rain', methods=['GET'])
def get_rain():
    return str(sim.weather.rain)


@app.route('/LGSVL/Status/Environment/TimeOfDay', methods=['GET'])
def get_timeofday():
    return str(sim.time_of_day)


@app.route('/LGSVL/Status/CollisionInfo', methods=['GET'])
def get_loc():
    """
    three types of collision: obstacle, NPC vehicle, pedestrian, None(no collision)
    :return:
    """
    global collision_object
    # print(collision_object)
    collision_info = str(collision_object)
    collision_object = None

    # convert collision information to one of three types
    collision_type = str(None)
    if collision_info == 'OBSTACLE':
        collision_type = "obstacle"
    if collision_info in npc_vehicle:
        collision_type = "npc_vehicle"
    if collision_info in pedestrian:
        collision_type = "pedestrian"

    print("Collision Info: ", collision_type)

    return collision_type


@app.route('/LGSVL/Status/EGOVehicle/Speed', methods=['GET'])
def get_speed():
    speed = "{:.2f}".format(sim.get_agents()[0].state.speed)
    # print(speed)
    return speed


@app.route('/LGSVL/Status/EGOVehicle/Position', methods=['GET'])
def get_position():
    # Vector(-9.02999973297119, -1.03600001335144, 50.3400001525879)
    position = sim.get_agents()[0].state.position
    # x_value = ":.2f".format(position.x)
    # y_value = ":.2f".format(position.y)
    # z_value = ":.2f".format(position.z)
    # format_position = "{:.2f}".format(position.x) + " {:.2f}".format(position.y) + " {:.2f}".format(position.z)
    # print(format_position)
    # return format_position

    pos_dict = {'x': position.x, 'y': position.y, 'z': position.z}
    return json.dumps(pos_dict)


@app.route('/LGSVL/Status/EGOVehicle/Position/X', methods=['GET'])
def get_position_x():
    position = sim.get_agents()[0].state.position
    return "{:.2f}".format(position.x)


@app.route('/LGSVL/Status/EGOVehicle/Position/Y', methods=['GET'])
def get_position_y():
    position = sim.get_agents()[0].state.position
    return "{:.2f}".format(position.y)


@app.route('/LGSVL/Status/EGOVehicle/Position/Z', methods=['GET'])
def get_position_z():
    position = sim.get_agents()[0].state.position
    return "{:.2f}".format(position.z)


"""
RESTful APIs for getting GPS data
"""


@app.route('/LGSVL/Status/GPSData', methods=['GET'])
def get_gps_data():
    gps_json = {}  # dict file, also can be defined by gps_json = dict()
    gps_data = sensors[1].data

    # String format: "{:.2f}".format(gps_data.latitude), "{:.2f}".format(gps_data.longitude)
    gps_json.update({'Altitude': round(gps_data.altitude, 2), 'Latitude': round(gps_data.latitude, 2),
                     'Longitude': round(gps_data.longitude, 2), 'Northing': round(gps_data.northing, 2),
                     'Easting': round(gps_data.easting, 2)})
    return json.dumps(gps_json)


@app.route('/LGSVL/Status/GPS/Latitude', methods=['GET'])
def get_latitude():
    gps_data = sensors[1].data
    latitude = "{:.2f}".format(gps_data.latitude)
    # print("Latitude:", latitude)
    return latitude


@app.route('/LGSVL/Status/GPS/Longitude', methods=['GET'])
def get_longitude():
    gps_data = sensors[1].data
    longitude = "{:.2f}".format(gps_data.longitude)
    # print("Latitude:", longitude)
    return longitude


@app.route('/LGSVL/Status/GPS/Altitude', methods=['GET'])
def get_altitude():
    gps_data = sensors[1].data
    altitude = "{:.2f}".format(gps_data.altitude)
    return altitude


@app.route('/LGSVL/Status/GPS/Northing', methods=['GET'])
def get_northing():
    gps_data = sensors[1].data
    northing = "{:.2f}".format(gps_data.northing)
    return northing


@app.route('/LGSVL/Status/GPS/Easting', methods=['GET'])
def get_easting():
    gps_data = sensors[1].data
    easting = "{:.2f}".format(gps_data.easting)
    return easting


"""
RESTful APIs for getting camera data
"""


@app.route('/LGSVL/Status/CenterCamera', methods=['GET'])
def get_main_camera_data():
    main = get_image('Main Camera')
    main_image_dict = {'index': main.tolist()}
    main_json = json.dumps(main_image_dict)
    return main_json


@app.route('/LGSVL/Status/LeftCamera', methods=['GET'])
def get_left_camera_data():
    left = get_image('Left Camera')
    left_image_dict = {'index': left.tolist()}
    left_json = json.dumps(left_image_dict)
    return left_json


@app.route('/LGSVL/Status/RightCamera', methods=['GET'])
def get_right_camera_data():
    right = get_image('Right Camera')
    right_image_dict = {'index': right.tolist()}
    right_json = json.dumps(right_image_dict)
    return right_json


@app.route('/LGSVL/Status/AllCamera', methods=['GET'])
def get_all_camera_data():
    main = get_image('Main Camera')
    # left = get_image('Left Camera')
    # right = get_image('Right Camera')
    # all_camera = np.hstack((main, left, right))
    # print(all_camera)
    all_camera_dict = {'index': main.tolist()}
    all_camera_json = json.dumps(all_camera_dict)
    return all_camera_json


@app.route('/LGSVL/Status/CollisionProbability', methods=['GET'])
def get_c_probability():
    global probability
    c_probability = probability
    probability = 0
    return str(c_probability)


@app.route('/LGSVL/Status/HardBrake', methods=['GET'])
def get_hard_brake():
    global speed_list
    acceleration_threshold = 8
    hard_brake = False
    speed = speed_list[0]
    for i in range(1, len(speed_list), 2):
        temp = speed_list[i]
        acceleration = abs(temp - speed) / 1
        speed = temp
        if acceleration >= acceleration_threshold:
            hard_brake = True
            break
    return json.dumps(hard_brake)


# @app.route('/LGSVL/Status/CollisionProbability', methods=['GET'])
# def get_collision_probability():
#     agents = sim.get_agents()
#     ego = agents[0]
#     ego_speed = ego.state.speed
#     ego_transform = ego.transform
#     global u
#     probability = 0
#     break_distance = calculate_safe_distance(ego_speed, u)
#     for i in range(1, len(agents)):
#         transform = agents[i].state.transform
#         current_distance = calculate_distance(transform.position, ego_transform.position)
#         print('current distance: ', current_distance)
#         if ego_transform.rotation.y - 10 < transform.rotation.y < ego_transform.rotation.y + 10:
#             # In this case, we can believe the ego vehicle and obstacle are on the same direction.
#             vector = transform.position - ego_transform.position
#             if ego_transform.rotation.y - 10 < calculate_angle(vector, z_axis) < ego_transform.rotation.y + 10:
#                 # In this case, we can believe the ego vehicle and obstacle are driving on the same lane.
#                 safe_distance = break_distance
#             else:
#                 # In this case, the ego vehicle and obstacle are not on the same lane. They are on two parallel lanes.
#                 safe_distance = 1
#         else:
#             # In this case, the ego vehicle and obstacle are either on the same direction or the same lane.
#             safe_distance = 5
#         new_probability = calculate_collision_probability(safe_distance, current_distance)
#         if new_probability > probability:
#             probability = new_probability
#     # print(probability)
#     return str(probability)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8933, debug=False)
